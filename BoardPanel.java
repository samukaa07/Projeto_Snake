package projeto.snake.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

	// classe painel
public class BoardPanel extends JPanel { // Criado essas variaveis finais para manter os valores sem alterações, ou seja constantes.
	
	 //Serial Version UID.
	private static final long serialVersionUID = -1102632585936750607L;

	// numero de colunas
	public static final int COL_COUNT = 25;
	
	//numero de linhas
	public static final int ROW_COUNT = 25;
	
	//tamanho de cada bloco
	public static final int TILE_SIZE = 25;
	
	//numero de pixels
	private static final int EYE_LARGE_INSET = TILE_SIZE / 3;
	
	// numero de pixels a frente
	private static final int EYE_SMALL_INSET = TILE_SIZE / 6;
	
	//tamanho dos olhos da cobra
	private static final int EYE_LENGTH = TILE_SIZE / 5;
	
	// fonte do Texto
	private static final Font FONT = new Font("Tahoma", Font.BOLD, 25);
		
	// Snakegame instancia
	private SnakeGame game;
	
	// Matriz do tabuleiro
	private TileType[] tiles;
		
	// aqui foi criado uma nova instancia BoardPanel 
	public BoardPanel(SnakeGame game) {
		this.game = game;
		this.tiles = new TileType[ROW_COUNT * COL_COUNT];
		
		setPreferredSize(new Dimension(COL_COUNT * TILE_SIZE, ROW_COUNT * TILE_SIZE)); // aqui agrupei a dimenssão coluna e linhas.
		setBackground(Color.BLACK); // usei o comando setBackground para pintar o fundo do painel
	}
	
	// Limpa todas as peças do tabuleiro e define seus valores como nulos.
	public void clearBoard() {
		for(int i = 0; i < tiles.length; i++) {
			tiles[i] = null;
		}
	}
	
	//coordenada dos pontos e typo do bloco
	public void setTile(Point point, TileType type) {
		setTile(point.x, point.y, type);
	}
	
		// as coordenadas  das colunas
	public void setTile(int x, int y, TileType type) {
		tiles[y * ROW_COUNT + x] = type;
	}
	
	// as coordenadas dos bloco
	public TileType getTile(int x, int y) {
		return tiles[y * ROW_COUNT + x];
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// usado for para percorrer as linhas e colunas do tabuleiro, usado tbm operador de igualdade != para trazer resultado não é null
		for(int x = 0; x < COL_COUNT; x++) {
			for(int y = 0; y < ROW_COUNT; y++) {
				TileType type = getTile(x, y);
				if(type != null) {
					drawTile(x * TILE_SIZE, y * TILE_SIZE, type, g);
				}
			}
		}
		
		// aqui feito o quadrado da área do jogo em retangulo
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		for(int x = 0; x < COL_COUNT; x++) {
			for(int y = 0; y < ROW_COUNT; y++) {
				g.drawLine(x * TILE_SIZE, 0, x * TILE_SIZE, getHeight());
				g.drawLine(0, y * TILE_SIZE, getWidth(), y * TILE_SIZE);
			}
		}		
		
		//aqui mostra as informações para cada caso no decorrer do jogo em letra vermelha
		if(game.isGameOver() || game.isNewGame() || game.isPaused()) {
			g.setColor(Color.RED);
			
			//coordenadas do centro = altura e largura
			int centerX = getWidth() / 2;
			int centerY = getHeight() / 2;
			
			//mensagens com base no estado do jogo
			String largeMessage = null;
			String smallMessage = null;
			if(game.isNewGame()) {
				largeMessage = "SNAKE GAME!";
				smallMessage = "Pressione ENTER para iniciar.";
			} else if(game.isGameOver()) {
				largeMessage = "Que Pena, você perdeu :(";
				smallMessage = "Aperte ENTER para Reiniciar.";
			} else if(game.isPaused()) {
				largeMessage = "Jogo Pausado !";
				smallMessage = "Aperte P para voltar ao jogo.";
			}
			
			// aqui definimos a fonte das mensagens no centro do quadro
			g.setFont(FONT);
			g.drawString(largeMessage, centerX - g.getFontMetrics().stringWidth(largeMessage) / 2, centerY - 50);
			g.drawString(smallMessage, centerX - g.getFontMetrics().stringWidth(smallMessage) / 2, centerY + 50);
		}
	}
	
	//coordenada x,y, tipo e grafico 
	private void drawTile(int x, int y, TileType type, Graphics g) {
		
		// usado comando switch para executar uma estrutura de decisão e testar as conições
		switch(type) {
		
		// case fruit do parametro oval de cor vermelha
		case Fruit:
			g.setColor(Color.RED);
			g.fillOval(x + 2, y + 2, TILE_SIZE - 4, TILE_SIZE - 4);
			break;
			
		case Fruit2:
			g.setColor(Color.RED);
			g.fillOval(x + 2, y + 2, TILE_SIZE - 4, TILE_SIZE - 4);
			break;
			
		case Fruit3:
			g.setColor(Color.GREEN);
			g.fillOval(x + 2, y + 2, TILE_SIZE - 4, TILE_SIZE - 4);
			break;
			
		case Fruit4:
			g.setColor(Color.GREEN);
			g.fillOval(x + 2, y + 2, TILE_SIZE - 4, TILE_SIZE - 4);
			break;
			
		// case SankeBody corpo da cobra cor verde
		case SnakeBody:
			g.setColor(Color.CYAN);
			g.fillOval(x, y, TILE_SIZE, TILE_SIZE); // fillrect função preencher quadrado
			break;
			
		// cabeça da cobra verde
		case SnakeHead:
			
			g.setColor(Color.CYAN);
			g.fillOval(x, y, TILE_SIZE, TILE_SIZE);
			
			// preto é o olho da cobra 
			g.setColor(Color.WHITE);
	
			
			switch(game.getDirection()) {
			case North: {
				int baseY = y + EYE_SMALL_INSET;
				g.drawLine(x + EYE_LARGE_INSET, baseY, x + EYE_LARGE_INSET, baseY + EYE_LENGTH);
				g.drawLine(x + TILE_SIZE - EYE_LARGE_INSET, baseY, x + TILE_SIZE - EYE_LARGE_INSET, baseY + EYE_LENGTH);
				break;
			}
				
			case South: {
				int baseY = y + TILE_SIZE - EYE_SMALL_INSET;
				g.drawLine(x + EYE_LARGE_INSET, baseY, x + EYE_LARGE_INSET, baseY - EYE_LENGTH);
				g.drawLine(x + TILE_SIZE - EYE_LARGE_INSET, baseY, x + TILE_SIZE - EYE_LARGE_INSET, baseY - EYE_LENGTH);
				break;
			}
			
			case West: {
				int baseX = x + EYE_SMALL_INSET;
				g.drawLine(baseX, y + EYE_LARGE_INSET, baseX + EYE_LENGTH, y + EYE_LARGE_INSET);
				g.drawLine(baseX, y + TILE_SIZE - EYE_LARGE_INSET, baseX + EYE_LENGTH, y + TILE_SIZE - EYE_LARGE_INSET);
				break;
			}
				
			case East: {
				int baseX = x + TILE_SIZE - EYE_SMALL_INSET;
				g.drawLine(baseX, y + EYE_LARGE_INSET, baseX - EYE_LENGTH, y + EYE_LARGE_INSET);
				g.drawLine(baseX, y + TILE_SIZE - EYE_LARGE_INSET, baseX - EYE_LENGTH, y + TILE_SIZE - EYE_LARGE_INSET);
				break;
			}
			
			}
			break;
		}
	}

}
