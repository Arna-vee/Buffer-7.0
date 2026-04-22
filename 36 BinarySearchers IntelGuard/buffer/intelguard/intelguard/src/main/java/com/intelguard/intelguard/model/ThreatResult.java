// model/ThreatResult.java
package com.intelguard.intelguard.model;

import java.util.List;

public class ThreatResult {

    private String agent;
    private String prompt;
    private int score;
    private String status;
    private String reason;
    private List<String> matchedWords;

    public ThreatResult(String agent,String prompt,int score,
                        String status,String reason,
                        List<String> matchedWords) {

        this.agent = agent;
        this.prompt = prompt;
        this.score = score;
        this.status = status;
        this.reason = reason;
        this.matchedWords = matchedWords;
    }

    public String getAgent() { return agent; }
    public String getPrompt() { return prompt; }
    public int getScore() { return score; }
    public String getStatus() { return status; }
    public String getReason() { return reason; }
    public List<String> getMatchedWords() { return matchedWords; }
}