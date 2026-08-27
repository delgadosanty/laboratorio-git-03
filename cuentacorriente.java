CuentaCorriente

public class CuentaCorriente extends CuentaBancaria {



private double limiteSobregiro;



public CuentaCorriente(String titular, double saldo) {

super(titular, saldo);

this.limiteSobregiro = 500.0;

}



public double getLimiteSobregiro() {

return limiteSobregiro;

}



public void setLimiteSobregiro(double limiteSobregiro) {

this.limiteSobregiro = limiteSobregiro;

}



@Override

public void aplicarComisionMensual() {

setSaldo(getSaldo() - 10.0);

}

} 

