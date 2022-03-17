package at.htlkaindorf.ahif18.lexer;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Token {

    private String lexeme;
    private TokenType type;
}
