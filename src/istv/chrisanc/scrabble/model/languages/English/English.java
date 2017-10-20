package istv.chrisanc.scrabble.model.languages.English;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.languages.English.letters.A;
import istv.chrisanc.scrabble.model.languages.English.letters.B;
import istv.chrisanc.scrabble.model.languages.English.letters.C;
import istv.chrisanc.scrabble.model.languages.English.letters.D;
import istv.chrisanc.scrabble.model.languages.English.letters.E;
import istv.chrisanc.scrabble.model.languages.English.letters.F;
import istv.chrisanc.scrabble.model.languages.English.letters.G;
import istv.chrisanc.scrabble.model.languages.English.letters.H;
import istv.chrisanc.scrabble.model.languages.English.letters.I;
import istv.chrisanc.scrabble.model.languages.English.letters.J;
import istv.chrisanc.scrabble.model.languages.English.letters.K;
import istv.chrisanc.scrabble.model.languages.English.letters.L;
import istv.chrisanc.scrabble.model.languages.English.letters.M;
import istv.chrisanc.scrabble.model.languages.English.letters.N;
import istv.chrisanc.scrabble.model.languages.English.letters.O;
import istv.chrisanc.scrabble.model.languages.English.letters.P;
import istv.chrisanc.scrabble.model.languages.English.letters.Q;
import istv.chrisanc.scrabble.model.languages.English.letters.R;
import istv.chrisanc.scrabble.model.languages.English.letters.S;
import istv.chrisanc.scrabble.model.languages.English.letters.T;
import istv.chrisanc.scrabble.model.languages.English.letters.U;
import istv.chrisanc.scrabble.model.languages.English.letters.V;
import istv.chrisanc.scrabble.model.languages.English.letters.W;
import istv.chrisanc.scrabble.model.languages.English.letters.X;
import istv.chrisanc.scrabble.model.languages.English.letters.Y;
import istv.chrisanc.scrabble.model.languages.English.letters.Z;
import istv.chrisanc.scrabble.model.languages.Global.letters.Joker;
import istv.chrisanc.scrabble.model.languages.Language;

import java.util.Arrays;
import java.util.List;

/**
 * @author Christopher Anciaux
 */
public class English extends Language {
    protected List<Class<? extends LetterInterface>> letters = Arrays.asList(A.class, B.class, C.class, D.class, E.class,
            F.class, G.class, H.class, I.class, J.class, K.class, L.class, M.class, N.class, O.class, P.class, Q.class, R.class,
            S.class, T.class, U.class, V.class, W.class, X.class, Y.class, Z.class, Joker.class);

    protected int[] lettersDistribution = new int[]{9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1, 2};

    public English() {
        super(EnglishDictionary.class);
    }

    protected List<Class<? extends LetterInterface>> getLetters() {
        return this.letters;
    }

    protected int[] getLettersDistribution() {
        return this.lettersDistribution;
    }
}
