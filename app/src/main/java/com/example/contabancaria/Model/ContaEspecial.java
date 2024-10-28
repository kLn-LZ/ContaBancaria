package com.example.contabancaria.Model;

public class ContaEspecial extends ContaBancaria {
    private float limite;

    public ContaEspecial(String cliente, int num_conta, float saldo, float limite) {
        super(cliente, num_conta, saldo);
        this.limite = limite;
    }

    @Override
    public void sacar(float valor) {
        if (valor > 0 && saldo + limite >= valor) {
            saldo -= valor;
        } else {
            System.out.println("Saldo insuficiente para saque, mesmo considerando o limite.");
        }
    }

    public float getLimite() {
        return limite;
    }
}
