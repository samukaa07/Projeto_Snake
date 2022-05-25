package projeto.snake.game;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JFrame;

// Classe SnakeGame responsavel por lidar com grande parte da logica do jogo
public class SnakeGame extends JFrame {
		
	// classe serialVersionUID para rastrear compatibilidade entre as classes
	private static final long serialVersionUID = 6678292058307426314L;

	// O número de milissegundos que devem passar entre cada quadro.
	private static final long FRAME_TIME = 1000L / 50L;
	
	// O comprimento mínimo da cobra. Isso permite que a cobra cresça ao redor do tabuleiro
	private static final int MIN_SNAKE_LENGTH = 1;
	
	// Numero maximo de direções
	private static final int MAX_DIRECTIONS = 3;
	
	//instancia BoardPanel
	private BoardPanel board;
	
	//A instância SidePanel.
	private SidePanel side;
	
	//O gerador de números aleatórios (usado para desova de frutas).
	private Random random;
	
	//A instância Clock para manipular a lógica do jogo.
	private Clock logicTimer;
	
	//Se estamos ou não executando um novo jogo.
	private boolean isNewGame;
		
	//Se o jogo acabou ou não.
	private boolean isGameOver;
	
	//Se o jogo está pausado ou não.
	private boolean isPaused;
	
	//	A lista que contém os pontos para a cobra.
	private LinkedList<Point> snake;
	
	//A lista que contém as rotas enfileiradas.
	private LinkedList<Direction> directions;
	
	//	A pontuação atual.
	private int score;
	
	// O número de frutas que comemos.
	private int fruitsEaten;
	
	// O número de pontos que a próxima fruta nos concederá.
	private int nextFruitScore;
	
	private int nextFruitScoreOne;
	
	
	private SnakeGame() { // criando uma nova instancia SnakeGame
		super("Snake Game");
		setLayout(new BorderLayout()); 
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
				
		//Inicializado os painéis do jogo e adicionado à janela.
		this.board = new BoardPanel(this);
		this.side = new SidePanel(this);
		
		add(board, BorderLayout.CENTER);
		add(side, BorderLayout.EAST);
		
		
		
		//Adiciona um novo ouvinte de chave ao quadro para processar a entrada.
		addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {

				// criado os casos de direção recente adjacente ao norte / cima
				case KeyEvent.VK_W:
				case KeyEvent.VK_UP:
					if(!isPaused && !isGameOver) { // Se o jogo não for pausado e o jogo não terminar...
						if(directions.size() < MAX_DIRECTIONS) {
							Direction last = directions.peekLast();
							if(last != Direction.South && last != Direction.North) {
								directions.addLast(Direction.North);
							}
						}
					}
					break;

			//// criado os casos de direção recente adjacente ao sul /baixo
				case KeyEvent.VK_S:
				case KeyEvent.VK_DOWN:
					if(!isPaused && !isGameOver) { // Se o jogo não for pausado e o jogo não terminar...
						if(directions.size() < MAX_DIRECTIONS) {
							Direction last = directions.peekLast();
							if(last != Direction.North && last != Direction.South) {
								directions.addLast(Direction.South);
							}
						}
					}
					break;
				
				// // criado os casos de direção recente adjacente a esquerda / leste	
				case KeyEvent.VK_A:
				case KeyEvent.VK_LEFT:
					if(!isPaused && !isGameOver) { // // Se o jogo não for pausado e o jogo não terminar...
						if(directions.size() < MAX_DIRECTIONS) {
							Direction last = directions.peekLast();
							if(last != Direction.East && last != Direction.West) {
								directions.addLast(Direction.West);
							}
						}
					}
					break;
			
				// // criado os casos de direção recente adjacente a direira / oeste
				case KeyEvent.VK_D:
				case KeyEvent.VK_RIGHT:
					if(!isPaused && !isGameOver) { // // Se o jogo não for pausado e o jogo não terminar...
						if(directions.size() < MAX_DIRECTIONS) {
							Direction last = directions.peekLast();
							if(last != Direction.West && last != Direction.East) {
								directions.addLast(Direction.East);
							}
						}
					}
					break;
				
			
				case KeyEvent.VK_P:
					if(!isGameOver) { // se o jogo não terminou
						isPaused = !isPaused;
						logicTimer.setPaused(isPaused); // atualize a pausa do tempo logicTimer
					}
					break;
				
				//	Reinicie o jogo se não estiver em andamento.
				case KeyEvent.VK_ENTER:
					if(isNewGame || isGameOver) {
						resetGame();
					}
					break;
				}
			}
			
		});
		
		//	Redimensione a janela para o tamanho apropriado, centralize-a na tela e exibi-lo.
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	// Inicia o jogo em execução.
	private void startGame() {
		
		//		Inicialize tudo que vamos usar.
		this.random = new Random();
		this.snake = new LinkedList<>();
		this.directions = new LinkedList<>();
		this.logicTimer = new Clock(9.0f);
		this.isNewGame = true;
		
		GameSound y = new GameSound();
		y.Song();
		
		//Defina o temporizador para pausado inicialmente.
		logicTimer.setPaused(true);

		//Este é o loop do jogo. Ele atualizará e renderizará o jogo e continuará a ser executado até que a janela do jogo seja fechada.
		while(true) {
			//Obtenha a hora de início do quadro atual.
			long start = System.nanoTime();
			
			//Atualize o temporizador lógico.
			logicTimer.update();
			
			//se um ciclo tiver decorrido no temporizador lógico, atualize o jogo.
			if(logicTimer.hasElapsedCycle()) {
				updateGame();
			}
			
			//Repinte a placa e o painel lateral com o novo conteúdo.
			board.repaint();
			side.repaint();
			
			// calculo do tempo inicial do inicio do quadro até o excedente
			long delta = (System.nanoTime() - start) / 1000000L;
			if(delta < FRAME_TIME) {
				try {
					Thread.sleep(FRAME_TIME - delta);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	private void updateGame() {
		
		// Obtém o tipo de ladrilho com o qual a cabeça da cobra colidiu. Se
		//a cobra bater em uma parede, SnakeBody será devolvido
		//  pois ambas as condições são tratados de forma idêntica.
		
		TileType collision = updateSnake();
		
		//Aqui lidamos com as diferentes colisões possíveis.
		
		//Fruta: Se colidimos com uma fruta, incrementamos o número de frutas que comemos, atualize a pontuação e gere uma nova fruta.
		
		//SnakeBody: Se colidimos com nossa cauda (ou uma parede), sinalizamos isso o jogo acabou e pause o jogo.
		
		// Se nenhuma colisão ocorreu, simplesmente decrementamos o número de pontos que a próxima fruta nos dará se for alta o suficiente.
		
		// Isso adiciona um	um pouco de habilidade para o jogo, pois coletar frutas mais rapidamente dar uma pontuação mais alta.
		
		if(collision == TileType.Fruit) {
			fruitsEaten++;
			score += nextFruitScore;
			spawnFruit();
		} else if(collision == TileType.SnakeBody) {
			isGameOver = true;
			logicTimer.setPaused(true);
		} else if(nextFruitScore > 10) {
			nextFruitScore--;
		}
	
	
	if(collision == TileType.Fruit2) {
		fruitsEaten++;
		score += nextFruitScore;
		spawnFruit2();
	} else if(collision == TileType.SnakeBody) {
		isGameOver = true;
		logicTimer.setPaused(true);
	} else if(nextFruitScore > 10) {
		nextFruitScore--;
	}

	
	if(collision == TileType.Fruit3) {
		fruitsEaten++;
		score += nextFruitScoreOne;
		spawnFruit3();
	} else if(collision == TileType.SnakeBody) {
		isGameOver = true;
		logicTimer.setPaused(true);
	} else if(nextFruitScoreOne > 10) {
		nextFruitScoreOne--;
		}
	
	if(collision == TileType.Fruit4) {
		fruitsEaten++;
		score += nextFruitScoreOne;
		spawnFruit4();
	} else if(collision == TileType.SnakeBody) {
		isGameOver = true;
		logicTimer.setPaused(true);
	} else if(nextFruitScoreOne > 10) {
		nextFruitScoreOne--;
	}
}
	//Atualiza a posição e o tamanho da cobra.
	private TileType updateSnake() {

		//Aqui nós espiamos a próxima direção ao invés de pesquisá-la. Enquanto não quebra o jogo, pesquisar a direção 
		//aqui causa um pequeno bug	 onde a direção da cobra mudará após o fim do jogo (embora	 não vai se mover).
		Direction direction = directions.peekFirst();
				
		// Aqui calculamos o novo ponto em que a cabeça da cobra estará após a atualização.	
		Point head = new Point(snake.peekFirst());
		switch(direction) {
		case North:
			head.y--;
			break;
			
		case South:
			head.y++;
			break;
			
		case West:
			head.x--;
			break;
			
		case East:
			head.x++;
			break;
		}
		
		//Se a cobra saiu dos limites ('bateu' em uma parede), podemos apenas retornar que colidiu consigo mesmo, 
		//pois ambos os casos são tratados identicamente.
		
		if(head.x < 0 || head.x >= BoardPanel.COL_COUNT || head.y < 0 || head.y >= BoardPanel.ROW_COUNT) {
			return TileType.SnakeBody; //Pretend we collided with our body.
		}
		
		
		//Aqui obtemos o ladrilho que estava localizado na nova posição da cabeça e	remova a cauda da cobra e a tábua 
		//se a cobra estiver tempo suficiente, e o ladrilho para o qual se moveu não é uma fruta.
		
		//Se a cauda foi removida, precisamos recuperar o ladrilho antigo novamente
		//caso o ladrilho que batemos fosse o pedaço de cauda que acabou de ser removido para evitar um falso game over.
		
		TileType old = board.getTile(head.x, head.y);
		if(old != TileType.Fruit && snake.size() > MIN_SNAKE_LENGTH) {
			Point tail = snake.removeLast();
			board.setTile(tail, null);
			old = board.getTile(head.x, head.y);
		}
		
		
		//Atualize a posição da cobra no tabuleiro se não colidimos com ela	nossa cauda:
		
		// 	1. Defina a posição antiga da cabeça para um bloco de corpo.
		// 2. Adicione a nova cabeça à cobra.
		//3. Defina a nova posição do cabeçote para um bloco de cabeçote.
		
		// Se mais de uma direção estiver na fila, faça uma sondagem para ler novas entrada.
		
		if(old != TileType.SnakeBody) {
			board.setTile(snake.peekFirst(), TileType.SnakeBody);
			snake.push(head);
			board.setTile(head, TileType.SnakeHead);
			if(directions.size() > 1) {
				directions.poll();
			}
			
		}
				
		return old;
	}
	
	/**
	 * Resets the game's variables to their default states and starts a new game.
	 */
	private void resetGame() {
		/*
		 Redefina as estatísticas de pontuação. (Observe que nextFruitPoints é redefinido em a função spawnFruit mais tarde).
		 */
		this.score = 0;
		this.fruitsEaten = 0;
		
		
		/*
		 * Redefina as novas bandeiras de jogo e game over.
		 */
		this.isNewGame = false;
		this.isGameOver = false;
		
		/*
		 * Crie a cabeça no centro do tabuleiro.
		 */
		Point head = new Point(BoardPanel.COL_COUNT / 2, BoardPanel.ROW_COUNT / 2);

		/*
		 * Limpe a lista de cobras e adicione a cabeça.
		 */
		snake.clear();
		snake.add(head);
		
		/*
		 * Limpe o tabuleiro e adicione a cabeça.
		 */
		board.clearBoard();
		board.setTile(head, TileType.SnakeHead);
		
		
		/*
		 * Limpe as direções e adicione o norte como odireção padrão.
		 */
		directions.clear();
		directions.add(Direction.North);
		
		/*
		 * Reinicialize o temporizador lógico.
		 */
		logicTimer.reset();
		
		/*
		 * Gera uma nova fruta.
		 */
		spawnFruit();
		spawnFruit2();
		spawnFruit3();
		spawnFruit4();
		
	}
	
	/**
	 *Obtém o sinalizador que indica se estamos jogando um novo jogo ou não.
	 */
	public boolean isNewGame() {
		return isNewGame;
	}
	
	/**
	 * Obtém o sinalizador que indica se o jogo acabou ou não.
	 */
	public boolean isGameOver() {
		return isGameOver;
	}
	
	/**
	 * Obtém o sinalizador que indica se o jogo está pausado ou não.
	 */
	public boolean isPaused() {
		return isPaused;
	}
	
	/**
	 * Gera uma nova fruta no tabuleiro.
	 */
	private void spawnFruit() {
		//configurei pontuação de 10 em 10.
		this.nextFruitScore = 10;
		
		

		/*
		 * Obtenha um índice aleatório com base no número de espaços livres deixados no tabuleiro.
		 */
		int index = random.nextInt(BoardPanel.COL_COUNT * BoardPanel.ROW_COUNT - snake.size());
		
		/*
		 * Embora pudéssemos facilmente escolher um índice aleatório no quadroe verifique se está livre até encontrarmos um vazio, 
		 * esse método tende a travar se a cobra ficar muito grande.
		 * 
		 * Este método simplesmente faz um loop até encontrar o enésimo índice livre e seleciona usa isso.
		 *  Isso significa que o jogo será capaz de localizar um índice a uma taxa relativamente constante, independentemente do
		 *   tamanho da cobra.
		 */
		int freeFound = -1;
		for(int x = 0; x < BoardPanel.COL_COUNT; x++) {
			for(int y = 0; y < BoardPanel.ROW_COUNT; y++) {
				TileType type = board.getTile(x, y);
				if(type == null || type == TileType.Fruit) {
					if(++freeFound == index) {
						board.setTile(x, y, TileType.Fruit);
						break;
					}
				}
			}
		}
	}
	
	
	private void spawnFruit2() {
		
		this.nextFruitScore = 10;
				
		int index = random.nextInt(BoardPanel.COL_COUNT * BoardPanel.ROW_COUNT - snake.size());
		
		int freeFound = -1;
		for(int x = 0; x < BoardPanel.COL_COUNT; x++) {
			for(int y = 0; y < BoardPanel.ROW_COUNT; y++) {
				TileType type = board.getTile(x, y);
				if(type == null || type == TileType.Fruit2) {
					if(++freeFound == index) {
						board.setTile(x, y, TileType.Fruit2);
						break;
					}
				}
			}
		}
	}
	private void spawnFruit3() {
		
		this.nextFruitScoreOne = 5;
				
		int index = random.nextInt(BoardPanel.COL_COUNT * BoardPanel.ROW_COUNT - snake.size());
		
		int freeFound = -1;
		for(int x = 0; x < BoardPanel.COL_COUNT; x++) {
			for(int y = 0; y < BoardPanel.ROW_COUNT; y++) {
				TileType type = board.getTile(x, y);
				if(type == null || type == TileType.Fruit3) {
					if(++freeFound == index) {
						board.setTile(x, y, TileType.Fruit3);
						break;
					}
				}
			}
		}
	}
	
	private void spawnFruit4() {
		
		this.nextFruitScoreOne = 5;
				
		int index = random.nextInt(BoardPanel.COL_COUNT * BoardPanel.ROW_COUNT - snake.size());
		
		int freeFound = -1;
		for(int x = 0; x < BoardPanel.COL_COUNT; x++) {
			for(int y = 0; y < BoardPanel.ROW_COUNT; y++) {
				TileType type = board.getTile(x, y);
				if(type == null || type == TileType.Fruit4) {
					if(++freeFound == index) {
						board.setTile(x, y, TileType.Fruit4);
						break;
					}
				}
			}
		}
	}
	
	
	/**
	 * Obtém a pontuação atual.
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * Obtém o número de frutas ingeridas.
	 */
	public int getFruitsEaten() {
		return fruitsEaten;
	}
	
	/**
	 * Obtém a próxima pontuação de frutas.
	 */
	public int getNextFruitScore() {
		return nextFruitScore;
	}
	
	public int getNextFruitScoreOne() {
		return nextFruitScoreOne;
	}
	/**
	 * Obtém a direção atual da cobra.
	 */
	public Direction getDirection() {
		return directions.peek();
	}
	
	/**
	 * Ponto de entrada do programa.
	 */
	public static void main(String[] args) {
		SnakeGame snake = new SnakeGame();
		snake.startGame();
		
		}

}
