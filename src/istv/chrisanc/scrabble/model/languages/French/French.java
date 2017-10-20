package istv.chrisanc.scrabble.model.languages.French;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.languages.French.letters.A;
import istv.chrisanc.scrabble.model.languages.French.letters.B;
import istv.chrisanc.scrabble.model.languages.French.letters.C;
import istv.chrisanc.scrabble.model.languages.French.letters.D;
import istv.chrisanc.scrabble.model.languages.French.letters.E;
import istv.chrisanc.scrabble.model.languages.French.letters.F;
import istv.chrisanc.scrabble.model.languages.French.letters.G;
import istv.chrisanc.scrabble.model.languages.French.letters.H;
import istv.chrisanc.scrabble.model.languages.French.letters.I;
import istv.chrisanc.scrabble.model.languages.French.letters.J;
import istv.chrisanc.scrabble.model.languages.French.letters.K;
import istv.chrisanc.scrabble.model.languages.French.letters.L;
import istv.chrisanc.scrabble.model.languages.French.letters.M;
import istv.chrisanc.scrabble.model.languages.French.letters.N;
import istv.chrisanc.scrabble.model.languages.French.letters.O;
import istv.chrisanc.scrabble.model.languages.French.letters.P;
import istv.chrisanc.scrabble.model.languages.French.letters.Q;
import istv.chrisanc.scrabble.model.languages.French.letters.R;
import istv.chrisanc.scrabble.model.languages.French.letters.S;
import istv.chrisanc.scrabble.model.languages.French.letters.T;
import istv.chrisanc.scrabble.model.languages.French.letters.U;
import istv.chrisanc.scrabble.model.languages.French.letters.V;
import istv.chrisanc.scrabble.model.languages.French.letters.W;
import istv.chrisanc.scrabble.model.languages.French.letters.X;
import istv.chrisanc.scrabble.model.languages.French.letters.Y;
import istv.chrisanc.scrabble.model.languages.French.letters.Z;
import istv.chrisanc.scrabble.model.languages.Global.letters.Joker;
import istv.chrisanc.scrabble.model.languages.Language;

import java.util.Arrays;
import java.util.List;

/**
 * @author Christopher Anciaux
 */
public class French extends Language {
    protected List<Class<? extends LetterInterface>> letters = Arrays.asList(A.class, B.class, C.class, D.class, E.class,
            F.class, G.class, H.class, I.class, J.class, K.class, L.class, M.class, N.class, O.class, P.class, Q.class, R.class,
            S.class, T.class, U.class, V.class, W.class, X.class, Y.class, Z.class, Joker.class);

    protected int[] lettersDistribution = new int[]{9, 2, 2, 3, 15, 2, 2, 2, 8, 1, 1, 5, 3, 6, 6, 2, 1, 6, 6, 6, 6, 2, 1, 1, 1, 1, 2};

    public French() {
        super(FrenchDictionary.class);
    }

    protected List<Class<? extends LetterInterface>> getLetters() {
        return this.letters;
    }

    protected int[] getLettersDistribution() {
        return this.lettersDistribution;
    }
}
