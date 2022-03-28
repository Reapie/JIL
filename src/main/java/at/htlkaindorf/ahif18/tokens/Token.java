package at.htlkaindorf.ahif18.tokens;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Token {

    private String lexeme;
    private TokenType type;
    private int lineNumber;

}
