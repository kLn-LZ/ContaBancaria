package com.example.contabancaria;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.contabancaria.Model.ContaBancaria;
import com.example.contabancaria.Model.ContaEspecial;
import com.example.contabancaria.Model.ContaPoupanca;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /*
     *@author: Kelvin Santos Guimarães
     */

    private List<ContaBancaria> contas;
    private RadioGroup rgTipoConta;
    private EditText edtCliente, edtNumConta, edtSaldo, edtValor, edtLimiteOuDia;
    private Button btnIncluir, btnSacar, btnDepositar, btnMostrarSaldo, btnPesquisar;
    private ContaBancaria contaSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RadioButton rbEspecial = findViewById(R.id.rbEspecial);
        rbEspecial.setChecked(true);

        contas = new ArrayList<>();
        rgTipoConta = findViewById(R.id.rgTipoConta);
        edtCliente = findViewById(R.id.edtCliente);
        edtNumConta = findViewById(R.id.edtNumConta);
        edtSaldo = findViewById(R.id.edtSaldo);
        edtValor = findViewById(R.id.edtValor);
        edtLimiteOuDia = findViewById(R.id.edtLimiteOuDia);
        btnIncluir = findViewById(R.id.btnIncluir);
        btnSacar = findViewById(R.id.btnSacar);
        btnDepositar = findViewById(R.id.btnDepositar);
        btnMostrarSaldo = findViewById(R.id.btnMostrarSaldo);
        btnPesquisar = findViewById(R.id.btnPesquisar);

        rgTipoConta.setOnCheckedChangeListener((radioGroup, id) -> {
            limparCampos();
        });

        btnIncluir.setOnClickListener(op -> {
            String cliente = edtCliente.getText().toString();
            int numConta = Integer.parseInt(edtNumConta.getText().toString());
            float saldo = Float.parseFloat(edtSaldo.getText().toString());
            int selectedId = rgTipoConta.getCheckedRadioButtonId();

            if (selectedId == R.id.rbPoupanca) {
                int diaDeRendimento = Integer.parseInt(edtLimiteOuDia.getText().toString());
                ContaPoupanca contaPoupanca = new ContaPoupanca(cliente, numConta, saldo, diaDeRendimento);
                contas.add(contaPoupanca);
                Toast.makeText(MainActivity.this, "Conta Poupança incluída com sucesso!", Toast.LENGTH_SHORT).show();
            } else if (selectedId == R.id.rbEspecial) {
                float limite = Float.parseFloat(edtLimiteOuDia.getText().toString());
                ContaEspecial contaEspecial = new ContaEspecial(cliente, numConta, saldo, limite);
                contas.add(contaEspecial);
                Toast.makeText(MainActivity.this, "Conta Especial incluída com sucesso!", Toast.LENGTH_SHORT).show();
            }
            limparCampos();
        });

        btnPesquisar.setOnClickListener(op -> {
            int numConta = Integer.parseInt(edtNumConta.getText().toString());
            contaSelecionada = null;
            for (ContaBancaria conta : contas) {
                if (conta.getNumConta() == numConta) {
                    contaSelecionada = conta;
                    break;
                }
            }
            if (contaSelecionada != null) {
                edtCliente.setText(contaSelecionada.getCliente());
                edtNumConta.setText(String.valueOf(contaSelecionada.getNumConta()));
                if (contaSelecionada instanceof ContaPoupanca) {
                    edtLimiteOuDia.setText(String.valueOf(((ContaPoupanca) contaSelecionada).getDiaDeRendimento()));
                    rgTipoConta.check(R.id.rbPoupanca);
                } else if (contaSelecionada instanceof ContaEspecial) {
                    edtLimiteOuDia.setText(String.valueOf(((ContaEspecial) contaSelecionada).getLimite()));
                    rgTipoConta.check(R.id.rbEspecial);
                }
                Toast.makeText(MainActivity.this, "Conta encontrada!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Conta não encontrada!", Toast.LENGTH_SHORT).show();
            }
        });

        btnSacar.setOnClickListener(op -> {
            if (contaSelecionada != null) {
                float valor = Float.parseFloat(edtValor.getText().toString());
                if (valor > contaSelecionada.getSaldo() && contaSelecionada instanceof ContaPoupanca) {
                    Toast.makeText(MainActivity.this, "Saldo insuficiente para saque.", Toast.LENGTH_SHORT).show();
                } else {
                    contaSelecionada.sacar(valor);
                    Toast.makeText(MainActivity.this, "Saque realizado com sucesso!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Por favor, pesquise uma conta primeiro!", Toast.LENGTH_SHORT).show();
            }
        });

        btnDepositar.setOnClickListener(op -> {
            if (contaSelecionada != null) {
                float valor = Float.parseFloat(edtValor.getText().toString());
                contaSelecionada.depositar(valor);
                Toast.makeText(MainActivity.this, "Depósito realizado com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Por favor, pesquise uma conta primeiro!", Toast.LENGTH_SHORT).show();
            }
        });

        btnMostrarSaldo.setOnClickListener(op -> {
            if (contaSelecionada != null) {
                if (contaSelecionada instanceof ContaPoupanca) {
                    ContaPoupanca contaPoupanca = (ContaPoupanca) contaSelecionada;
                    float taxaRendimento = contaPoupanca.getDiaDeRendimento() * 0.1f;
                    contaPoupanca.calcularNovoSaldo(taxaRendimento);
                }
                TextView tvSaldo = findViewById(R.id.tvSaldo);
                tvSaldo.setText("Saldo atual: " + contaSelecionada.getSaldo());
            } else {
                Toast.makeText(MainActivity.this, "Por favor, pesquise uma conta primeiro!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void limparCampos() {
        edtCliente.setText("");
        edtNumConta.setText("");
        edtSaldo.setText("");
        edtValor.setText("");
        edtLimiteOuDia.setText("");
    }
}