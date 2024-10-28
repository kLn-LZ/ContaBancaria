package com.example.contabancaria.Model;

public abstract class ContaBancaria {
    protected String cliente;
    protected int num_conta;
    protected float saldo;

    public ContaBancaria(String cliente, int num_conta, float saldo) {
        this.cliente = cliente;
        this.num_conta = num_conta;
        this.saldo = saldo;
    }

    public void sacar(float valor) {
        if (valor > 0 && saldo >= valor) {
            saldo -= valor;
        } else {
            System.out.println("Saldo insuficiente para saque.");
        }
    }

    public void depositar(float valor) {
        if (valor > 0) {
            saldo += valor;
        } else {
            System.out.println("Valor de depósito inválido.");
        }
    }

    public float getSaldo() {
        return saldo;
    }

    public int getNumConta() {
        return num_conta;
    }

    public String getCliente() {
        return cliente;
    }
}

