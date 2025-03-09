package HuffmanProject;

public class HuffmanTableRow {
    private final Integer asciiCode;
    private final String character;
    private final Integer frequency;
    private final String huffmanCode;
    private final Integer huffmanSize;

    public HuffmanTableRow(int asciiCode, String character, int frequency, String huffmanCode, int huffmanSize) {
        this.asciiCode = asciiCode;
        this.character = character;
        this.frequency = frequency;
        this.huffmanCode = huffmanCode;
        this.huffmanSize = huffmanSize;
    }

    public Integer getAsciiCode() {
        return asciiCode;
    }

    public String getCharacter() {
        return character;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public String getHuffmanCode() {
        return huffmanCode;
    }

    public Integer getHuffmanSize() {
        return huffmanSize;
    }
}
