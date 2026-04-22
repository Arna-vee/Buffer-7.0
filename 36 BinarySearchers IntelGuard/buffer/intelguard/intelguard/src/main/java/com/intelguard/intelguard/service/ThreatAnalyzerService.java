package com.intelguard.intelguard.service;

import com.intelguard.intelguard.model.ThreatResult;
import com.intelguard.intelguard.dsa.Trie;
import org.springframework.stereotype.Service;

import java.util.*;
import java.io.InputStream;

@Service
public class ThreatAnalyzerService {

    private HashMap<String, List<String>> permissions = new HashMap<>();
    private List<ThreatResult> logs = new ArrayList<>();

    // ✅ Trie cache
    private Map<String, Trie> trieCache = new HashMap<>();

    // 🚨 Critical words (instant block)
    private static final Set<String> CRITICAL_WORDS = Set.of(
            "hack", "breach", "exploit", "bypass", "attack"
    );

    // 🔥 Sliding Window config (60 sec)
    private static final long WINDOW_TIME = 60000; // 60 sec
    private static final int MAX_REQUESTS = 5;

    // 🔥 Sliding Window storage
    private Map<String, Queue<Long>> requestTimes = new HashMap<>();

    public ThreatAnalyzerService() {

        permissions.put("HR Agent",
                Arrays.asList("employee", "attendance", "leave"));

        permissions.put("Finance Agent",
                Arrays.asList("salary", "payroll", "invoice", "expenses"));

        permissions.put("IT Agent",
                Arrays.asList("logs", "devices", "system", "network"));
    }

    // ✅ Get cached Trie
    public Trie getTrie(String agent) {

        if (trieCache.containsKey(agent)) {
            return trieCache.get(agent);
        }

        Trie trie = new Trie();

        loadFile(trie, "commonthreatwords.txt");

        if (agent.equals("HR Agent"))
            loadFile(trie, "hr_words.txt");

        else if (agent.equals("Finance Agent"))
            loadFile(trie, "finance_words.txt");

        else if (agent.equals("IT Agent"))
            loadFile(trie, "it_words.txt");

        trieCache.put(agent, trie);

        return trie;
    }

    public void loadFile(Trie trie, String fileName) {

        try {

            InputStream input =
                    getClass().getClassLoader()
                            .getResourceAsStream(fileName);

            Scanner sc = new Scanner(input);

            while (sc.hasNextLine()) {

                String word =
                        sc.nextLine().trim().toLowerCase();

                if (!word.isEmpty())
                    trie.insert(word);
            }

            sc.close();

        } catch (Exception e) {
            System.out.println("File missing: " + fileName);
        }
    }

    public ThreatResult analyze(String agent, String prompt) {

        Trie trie = getTrie(agent);

        prompt = prompt.toLowerCase();

        int score = 0;
        String reason = "Normal Request";

        List<String> matched = new ArrayList<>();

        String[] arr = prompt.split(" ");

        // 🚨 1. CRITICAL OVERRIDE
        for (String word : arr) {

            word = word.replaceAll("[^a-z]", "");

            if (CRITICAL_WORDS.contains(word)) {

                matched.add(word);

                ThreatResult result =
                        new ThreatResult(
                                agent,
                                prompt,
                                100,
                                "BLOCKED 🚨",
                                "Critical Security Threat: " + word,
                                matched
                        );

                logs.add(result);
                return result;
            }
        }

        // 🔍 2. Trie keyword matching
        for (String word : arr) {

            word = word.replaceAll("[^a-z]", "");

            if (trie.search(word)) {
                matched.add(word);
                score += 20;
            }
        }

        // 🔐 3. Permission checks
        List<String> allowed = permissions.get(agent);

        if (prompt.contains("salary") &&
                !allowed.contains("salary")) {

            score += 40;
            reason = "Unauthorized Salary Access";
        }

        if (prompt.contains("payroll") &&
                !allowed.contains("payroll")) {

            score += 40;
            reason = "Privilege Abuse Attempt";
        }

        // 🔥 4. Sliding Window (60 sec) → GUARANTEED BLOCK
        long now = System.currentTimeMillis();

        requestTimes.putIfAbsent(agent, new LinkedList<>());
        Queue<Long> q = requestTimes.get(agent);

        q.add(now);

        // remove old timestamps
        while (!q.isEmpty() && now - q.peek() > WINDOW_TIME) {
            q.poll();
        }

        // DEBUG (optional)
        System.out.println("Requests in window: " + q.size());

        // 🚨 Burst detection (instant block)
        if (q.size() > MAX_REQUESTS) {

            ThreatResult result =
                    new ThreatResult(
                            agent,
                            prompt,
                            100,
                            "BLOCKED 🚨",
                            "Burst Activity Detected (60s Window)",
                            matched
                    );

            logs.add(result);
            return result;
        }

        // ⚠️ 5. General threat reason (DON'T override burst)
        if (!matched.isEmpty() && reason.equals("Normal Request")) {
            reason = "Prompt Injection / Threat Detected";
        }

        // 📊 6. Final decision
        String status =
                score >= 40 ?
                        "BLOCKED 🚨" :
                        "ALLOWED ✅";

        ThreatResult result =
                new ThreatResult(
                        agent,
                        prompt,
                        score,
                        status,
                        reason,
                        matched
                );

        logs.add(result);

        return result;
    }

    public List<ThreatResult> getLogs() {
        return logs;
    }
}