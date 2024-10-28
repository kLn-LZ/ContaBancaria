package com.example.contabancaria.Model;

public class ContaPoupanca extends ContaBancaria {
    private int diaDeRendimento;

    public ContaPoupanca(String cliente, int num_conta, float saldo, int diaDeRendimento) {
        super(cliente, num_conta, saldo);
        this.diaDeRendimento = diaDeRendimento;
    }

    public void calcularNovoSaldo(float taxaRendimento) {
        if (taxaRendimento > 0) {
            saldo += saldo * (taxaRendimento / 100);
        } else {
            System.out.println("Taxa de rendimento inválida.");
        }
    }

    public int getDiaDeRendimento() {
        return diaDeRendimento;
    }
}
