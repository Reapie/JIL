package at.htlkaindorf.ahif18.tokens;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The base class of a token containing more its core details
 *
 * @author Martin Juritsch
 * @version 1.0
 * @since 1.0
 */
@Data
@AllArgsConstructor
public class Token {

    private String lexeme;
    private TokenType type;
    private int lineNumber;

}
