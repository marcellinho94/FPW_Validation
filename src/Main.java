
public class Main {

	public static void main(String[] args) {

		Double banca = 20.0;
		Double porcentagemProfit = 60.0;
		Double porcentagemOperacao = 1.0;
		Double meta = 10000000.0;
		Integer qtdOPe = 0;

		calcula(banca, porcentagemProfit, porcentagemOperacao, meta, qtdOPe);
	}

	public static int calcula(Double banca, Double porcentagemProfit, Double porcentagemOperacao, Double meta,
			Integer qtdOPe) {

		qtdOPe = qtdOPe + 1;
		Double valOpe = banca * porcentagemOperacao / 100;
		Double lucro = (valOpe * porcentagemProfit / 100);
		banca = banca + lucro; 

		System.out.println("S: " + qtdOPe +" Banca: " + String.format("%.2f", banca) + " Valor Ope: " + String.format("%.2f", valOpe) + " Lucro Ope: " + String.format("%.2f", lucro));

		if (banca >= meta) {
			return qtdOPe;
		} else {
			return calcula(banca, porcentagemProfit, porcentagemOperacao, meta, qtdOPe);
		}
	}
}
