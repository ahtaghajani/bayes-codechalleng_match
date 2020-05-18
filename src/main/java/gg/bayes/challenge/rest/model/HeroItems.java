package gg.bayes.challenge.rest.model;

import lombok.Data;

@Data
public class HeroItems {
    private String item;
    private Long timestamp;

    public void setItem(String item) {
        this.item = item;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
