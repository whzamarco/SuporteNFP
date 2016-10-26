package frames;


import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Home extends JFrame {

	private static final long serialVersionUID = 1L;
	ArrayList<String> log;
	private JPanel contentPane;
	File arq_entrada = null;
	private JTextField tfEntrada;
	private JTextField tfSaida;
	File caminho_arq_sai = null;
	String nome_arq_sai = null;
	JFrame frame;
	DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	ArrayList<Date> datas = new ArrayList<Date>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home frame = new Home();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Home() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Home.class.getResource("/Imagens/logo.png")));
		setTitle("Suporte NF Paulistana V4.0");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		tfEntrada = new JTextField();
		tfEntrada.setBounds(176, 55, 127, 20);
		contentPane.add(tfEntrada);
		tfEntrada.setColumns(10);

		JButton bentrada = new JButton("...");
		bentrada.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser fc = new JFileChooser();
				File diretorio = new File("c:\\");
				fc.setCurrentDirectory(diretorio);
				fc.showOpenDialog(Home.this);
				if (fc.getSelectedFile() != null) {
					arq_entrada = fc.getSelectedFile();
					tfEntrada.setText(arq_entrada.getName());
				}

			}
		});
		bentrada.setBounds(324, 52, 45, 23);
		contentPane.add(bentrada);

		tfSaida = new JTextField();
		tfSaida.setBounds(176, 110, 127, 20);
		contentPane.add(tfSaida);
		tfSaida.setColumns(10);

		JButton bsaida = new JButton("...");
		bsaida.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				File arquivo_saida = null;
				File diretorio = new File("c:\\");
				File nome = new File("Notas.txt");
				fc.setCurrentDirectory(diretorio);
				fc.setSelectedFile(nome);
				fc.showOpenDialog(Home.this);
				caminho_arq_sai = fc.getCurrentDirectory();
				arquivo_saida = fc.getSelectedFile();
				tfSaida.setText(arquivo_saida.getName());
				nome_arq_sai = arquivo_saida.getName();
			}
		});
		bsaida.setBounds(324, 107, 45, 23);
		contentPane.add(bsaida);

		JLabel lblArquivoDeEntrada = new JLabel("Arquivo de Entrada:");
		lblArquivoDeEntrada.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblArquivoDeEntrada.setBounds(26, 58, 120, 14);
		contentPane.add(lblArquivoDeEntrada);

		JLabel lblArquivoDeSada = new JLabel("Arquivo de Sa\u00EDda:");
		lblArquivoDeSada.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblArquivoDeSada.setBounds(26, 113, 120, 14);
		contentPane.add(lblArquivoDeSada);

		JButton btnAjuda = new JButton("");
		btnAjuda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame,
						"Domínio Siatemas - Suporte Regional Sudeste\n" +
						"V1.0 - Valida CPF e CNPJ\n" +
						"V2.0 - Ajusta tamanho da linha\n" +
						"V3.0 - Verifica dados do RPS e remove quando não possui\n" +
						"V4.0 - Verifica Menor e Maior data dos RPS do arquivo");
			}
		});
		btnAjuda.setIcon(new ImageIcon(Home.class.getResource("/Imagens/ajuda.png")));
		btnAjuda.setBounds(373, 214, 51, 37);
		contentPane.add(btnAjuda);
		
		JButton btnConverte = new JButton("Converter");
		btnConverte.setIcon(new ImageIcon(Home.class.getResource("/Imagens/converte.png")));
		btnConverte.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!tfEntrada.getText().equals("") && !tfSaida.getText().equals("")){
					ArrayList<String> arquivo_final = programacao(abre_arquivo(arq_entrada.toString()));
					dataRPS(arquivo_final);
					grava_arquivo(caminho_arq_sai.toString(), nome_arq_sai, arquivo_final);
					grava_arquivo(caminho_arq_sai.toString(), "log.txt", log);
				}else{
					JOptionPane.showMessageDialog(frame,"Informe os arquivos de entrada e saída.");
				}
				
			}
		});
		btnConverte.setBounds(164, 201, 120, 23);
		contentPane.add(btnConverte);
	}

	private void dataRPS(ArrayList<String> arquivo){
		
		for (int i = 0; i < arquivo.size(); i++) {
			if (arquivo.get(i).substring(0,1).trim().equals("2")) {
				try {
					this.datas.add((Date)this.formatter.parse(arquivo.get(i).substring(53,61).trim()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else;
			
		}
		Date maior = this.datas.get(0);
		Date menor = this.datas.get(0);
		
		for (int i = 0; i < this.datas.size(); i++) {
			if (this.datas.get(i).after(maior)) {
				maior = this.datas.get(i);
			}else;
			if (this.datas.get(i).before(menor)) {
				menor = this.datas.get(i);
			}else;
		}
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		this.log.add("Menor data de RPS: "+ formato.format(menor));
		this.log.add("Maior data de RPS: "+ formato.format(maior));
		System.out.println("Menor data de RPS: "+ formato.format(menor));
		System.out.println("Maior data de RPS: "+ formato.format(maior));
	}
	
	
	//Metodo para abrir arquivo em memoria
	private ArrayList<String> abre_arquivo(String caminho_arq_ent){
		String linha;
		ArrayList<String> leitura = new ArrayList<String>();
		try {
            FileReader fileReader = new FileReader(caminho_arq_ent);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while( (linha = bufferedReader.readLine()) != null){
            	leitura.add(linha);
            }
            fileReader.close();
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("Erro abrindo arquivo");
        }
		System.out.println("Arquivo aberto!");
		return leitura;
	}
	
	//Metoddo para gravar o arquivo em disco
	private void grava_arquivo(String caminho, String arqui_sai,ArrayList<String> gravar){
		File arq = new File(caminho, arqui_sai);
		try {
            arq.createNewFile();
            
            //FileWriter fileWriter = new FileWriter(arq, false);
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(arq)));
            //PrintWriter printWriter = new PrintWriter(fileWriter);
            for(int i=0;i<gravar.size();i++ ){
            	printWriter.println(gravar.get(i));
            }
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
        	System.out.println("Erro ao gravar o arquivo, Erro 1.");
        }
	}

	private ArrayList<String> programacao(ArrayList<String> arquivo_bruto){
		ArrayList<String> arquivo_lapidado = new ArrayList<String>();
		this.log = new ArrayList<String>();
		String linha = "";
		String linha_manipulada = "";
		String DataRPSLinhaAnterior = "";
		
		for(int i=0; i < arquivo_bruto.size() ; i++){
			linha = arquivo_bruto.get(i);
			if(!linha.equals("")){
				if(linha.substring(0, 1).equals("2")){
					
					//Verifica se CPF é valido e caso NÃO seja substitui por 300000000000000, este 3 é referente ao tipo de inscrição
					if (linha.substring(517, 518).equals("1")) {
						String cpf = linha.substring(521, 532);
						if(!VerificaCPF.isValidCPF(cpf)){
							linha_manipulada = linha.substring(0, 517)+ "300000000000000" + linha.substring(532, linha.length());
							this.log.add("Linha "+(i+1)+" CPF invalido: "+cpf+".");
							System.out.println("Linha "+(i+1)+" CPF invalido: "+cpf+".");
						}else{
							linha_manipulada = linha;
						}
					}
					//Verifica se CNPJ é valido e caso NÃO seja substitui por 300000000000000, este 3 é referente ao tipo de inscrição
					if (linha.substring(517, 518).equals("2")) {
						String cnpj = linha.substring(518, 532);
						if(!VerificaCPF.isValidCNPJ(cnpj)){
							linha_manipulada = linha.substring(0, 517)+ "300000000000000" + linha.substring(532, linha.length());
							this.log.add("Linha "+(i+1)+" CNPJ invalido: "+cnpj+".");
							System.out.println("Linha "+(i+1)+" CNPJ invalido: "+cnpj+".");
						}else{
							linha_manipulada = linha;
						}
					}
					//Verifica se o tamanho da linha excede 1886, caso SIM corta a linha
					if (linha.length() > 1886) {
						linha_manipulada = linha.substring(0, 1886);
						this.log.add("Linha "+(i+1)+" maior que 1886");
					}else;
					
					//Data RPS
					/*if(linha.substring(53,61).trim().equals("")){
						linha_manipulada = linha.substring(0, 53)+ DataRPSLinhaAnterior + linha.substring(61, linha.length());
						this.log.add("Linha "+(i+1)+" Alterada a Data RPS para: "+DataRPSLinhaAnterior+".");
						System.out.println("Linha "+(i+1)+" Alterada a Data RPS para: "+DataRPSLinhaAnterior+".");
					}else{
						DataRPSLinhaAnterior = linha.substring(53,61);
					}*/
					//Verifica Numero e Data RPS
					if(linha.substring(53,61).trim().equals("")||linha.substring(41,53).trim().equals("000000000000")){
						linha_manipulada = "";
						this.log.add("Linha "+(i+1)+" Removida do arquivo porque não possui numero ou data do RPS.");
						System.out.println("Linha "+(i+1)+" Removida do arquivo porque não possui numero ou data do RPS.");
					}else;
					//Periodo de data do arquivo
					
					
				}else{
					linha_manipulada = linha;
				}
				if (!linha_manipulada.equals("")) {
					arquivo_lapidado.add(linha_manipulada);
				}else;
			}
			
		}
		return arquivo_lapidado;
	}

	
}
