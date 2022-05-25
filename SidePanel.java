package projeto.snake.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

	// Classe SidePanel é responsável por exibir estatísticas e controles para o jogador.
public class SidePanel extends JPanel {
	
	// classe serialVersionUID para rastrear compatibilidade entre as classes
	private static final long serialVersionUID = -40557434900946408L;

	// tamanho da fonte formato grande e modelo
	private static final Font LARGE_FONT = new Font("Gabriola", Font.BOLD, 40);
	
	// tamando da fonte média e modelo
	private static final Font MEDIUM_FONT = new Font("Gabriola", Font.BOLD, 25);

	// tamanho da fonte pequena e modelo
	private static final Font SMALL_FONT = new Font("Cambria", Font.BOLD, 12);
	
	
	private SnakeGame game;
	
	//criado a instancia Sidepanel
	public SidePanel(SnakeGame game) {
		this.game = game;
		
		setPreferredSize(new Dimension(300, BoardPanel.ROW_COUNT * BoardPanel.TILE_SIZE)); // dimensões da tela
		setBackground(Color.blue); // cor do fundo da tela
	}
	
	// medidas pré estabelecidas
	private static final int STATISTICS_OFFSET = 350;
	
	private static final int CONTROLS_OFFSET = 450;
	
	private static final int MESSAGE_STRIDE = 30;
	
	private static final int MESSAGEE_STRIDE = 20;
	
	private static final int SMALL_OFFSET = 50;
	
	private static final int LARGE_OFFSET = 50;
	
	private static final int Dicas = 250;
	
	private static final int Desafio = 150;
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// cor da fonte
		g.setColor(Color.BLACK);
		
		// Nome do jogo 
		g.setFont(LARGE_FONT);
		g.drawString(" #| Projeto final |#", getWidth() / 5 - g.getFontMetrics().stringWidth("Snake Game") / 5, 50);
		g.setColor(Color.ORANGE);
		g.drawString(" SNAKE GAME ", getWidth() / 3 - g.getFontMetrics().stringWidth("Snake Game") / 3, 100);
		
		g.setColor(Color.GREEN); 
		
		// Desenhando as categorias Estatisticas e controles 
		g.setFont(MEDIUM_FONT);
		g.drawString("Desafio : ", SMALL_OFFSET, Desafio);
		g.drawString("Dicas : ", SMALL_OFFSET, Dicas);
		g.drawString("Estatisticas : ", SMALL_OFFSET, STATISTICS_OFFSET);
		g.drawString("Controles : ", SMALL_OFFSET, CONTROLS_OFFSET);
				
		//fonte da letra
		g.setFont(SMALL_FONT);
		
		g.setColor(Color.white);
		
		// Desenhado o conteúdo para a categoria de estatísticas.
		int drawwwY = Desafio;
		g.drawString("Sobreviver sem se chocar " , LARGE_OFFSET, drawwwY += MESSAGEE_STRIDE);
		g.drawString("e se alimentar da comida " , LARGE_OFFSET, drawwwY += MESSAGEE_STRIDE);
		g.drawString("acumulando pontos !!! " , LARGE_OFFSET, drawwwY += MESSAGEE_STRIDE);
		
		int drawwY = Dicas;
		g.drawString("Maça = 10 Pontos: " , LARGE_OFFSET, drawwY += MESSAGE_STRIDE);
		g.drawString("Pera = 05 Pontos: " , LARGE_OFFSET, drawwY += MESSAGE_STRIDE);
		
		int drawY = STATISTICS_OFFSET;
		g.drawString("Pontuação: " + game.getScore(), LARGE_OFFSET, drawY += MESSAGE_STRIDE);
		g.drawString("Frutas comidas: " + game.getFruitsEaten(), LARGE_OFFSET, drawY += MESSAGE_STRIDE);
		//g.drawString("Pontuação das Frutas: " + game.getNextFruitScore(), LARGE_OFFSET, drawY += MESSAGE_STRIDE);
		
		//Desenhado o conteúdo para a categoria de controles.
		drawY = CONTROLS_OFFSET;
		g.drawString("Pausar Jogo : P", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
		g.drawString("Move direita: D ", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
		g.drawString("Move esquerda: A ", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
		g.drawString("Mover cima: W ", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
		g.drawString("Mover baixo: S ", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
									
	}

}
