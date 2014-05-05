package bio.tit.narise.rsas.model.factory.product;

import java.math.BigInteger;

/**
 *
 * @author TN
 */
public class ChooseRes extends Result {

    @Override
    public Result add(Result res) {
        throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private BigInteger result;
    
    public ChooseRes (BigInteger bigInt) {
        this.result = bigInt;
    }

    /**
     * @return the result
     */
    public BigInteger getResult() {
        return result;
    }
    
}
