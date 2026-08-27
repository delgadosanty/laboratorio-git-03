CuentaAhorros

public class CuentaAhorros extends CuentaBancaria {



public CuentaAhorros(String titular, double saldo) {

super(titular, saldo);

}



@Override

public void aplicarComisionMensual() {

setSaldo(getSaldo() - 5.0);

}

}
