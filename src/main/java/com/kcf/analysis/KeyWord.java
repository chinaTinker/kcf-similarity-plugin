package com.kcf.analysis;

/**
 * Author: 老牛 -- TK
 * Date:   14-4-11
 */
class KeyWord {
    private String name;

    private String nature;

    private int frequency;

    KeyWord() {}

    KeyWord(String name, String nature, int frequency) {
        this.name = name;
        this.nature = nature;
        this.frequency = frequency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
