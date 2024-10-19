package spring.educhainminiapp.dto;

public class UserDto {
    private int level;
    private String username;
    private int tokens;

    public UserDto(int level, String username, int tokens) {
        this.level = level;
        this.username = username;
        this.tokens = tokens;
    }

    public int getLevel() {
        return level;
    }

    public String getUsername() {
        return username;
    }

    public int getTokens() {
        return tokens;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }
}
