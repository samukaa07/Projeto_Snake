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

	// O n�mero de milissegundos que devem passar entre cada quadro.
	private static final long FRAME_TIME = 1000L / 50L;
	
	// O comprimento m�nimo da cobra. Isso permite que a cobra cres�a ao redor do tabuleiro
	private static final int MIN_SNAKE_LENGTH = 1;
	
	// Numero maximo de dire��es
	private static final int MAX_DIRECTIONS = 3;
	
	//instancia BoardPanel
	private BoardPanel board;
	
	//A inst�ncia SidePanel.
	private SidePanel side;
	
	//O gerador de n�meros aleat�rios (usado para desova de frutas).
	private Random random;
	
	//A inst�ncia Clock para manipular a l�gica do jogo.
	private Clock logicTimer;
	
	//Se estamos ou n�o executando um novo jogo.
	private boolean isNewGame;
		
	//Se o jogo acabou ou n�o.
	private boolean isGameOver;
	
	//Se o jogo est� pausado ou n�o.
	private boolean isPaused;
	
	//	A lista que cont�m os pontos para a cobra.
	private LinkedList<Point> snake;
	
	//A lista que cont�m as rotas enfileiradas.
	private LinkedList<Direction> directions;
	
	//	A pontua��o atual.
	private int score;
	
	// O n�mero de frutas que comemos.
	private int fruitsEaten;
	
	// O n�mero de pontos que a pr�xima fruta nos conceder�.
	private int nextFruitScore;
	
	private int nextFruitScoreOne;
	
	
	private SnakeGame() { // criando uma nova instancia SnakeGame
		super("Snake Game");
		setLayout(new BorderLayout()); 
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
				
		//Inicializado os pain�is do jogo e adicionado � janela.
		this.board = new BoardPanel(this);
		this.side = new SidePanel(this);
		
		add(board, BorderLayout.CENTER);
		add(side, BorderLayout.EAST);
		
		
		
		//Adiciona um novo ouvinte de chave ao quadro para processar a entrada.
		addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {

				// criado os casos de dire��o recente adjacente ao norte / cima
				case KeyEvent.VK_W:
				case KeyEvent.VK_UP:
					if(!isPaused && !isGameOver) { // Se o jogo n�o for pausado e o jogo n�o terminar...
						if(directions.size() < MAX_DIRECTIONS) {
							Direction last = directions.peekLast();
							if(last != Direction.South && last != Direction.North) {
								directions.addLast(Direction.North);
							}
						}
					}
					break;

			//// criado os casos de dire��o recente adjacente ao sul /baixo
				case KeyEvent.VK_S:
				case KeyEvent.VK_DOWN:
					if(!isPaused && !isGameOver) { // Se o jogo n�o for pausado e o jogo n�o terminar...
						if(directions.size() < MAX_DIRECTIONS) {
							Direction last = directions.peekLast();
							if(last != Direction.North && last != Direction.South) {
								directions.addLast(Direction.South);
							}
						}
					}
					break;
				
				// // criado os casos de dire��o recente adjacente a esquerda / leste	
				case KeyEvent.VK_A:
				case KeyEvent.VK_LEFT:
					if(!isPaused && !isGameOver) { // // Se o jogo n�o for pausado e o jogo n�o terminar...
						if(directions.size() < MAX_DIRECTIONS) {
							Direction last = directions.peekLast();
							if(last != Direction.East && last != Direction.West) {
								directions.addLast(Direction.West);
							}
						}
					}
					break;
			
				// // criado os casos de dire��o recente adjacente a direira / oeste
				case KeyEvent.VK_D:
				case KeyEvent.VK_RIGHT:
					if(!isPaused && !isGameOver) { // // Se o jogo n�o for pausado e o jogo n�o terminar...
						if(directions.size() < MAX_DIRECTIONS) {
							Direction last = directions.peekLast();
							if(last != Direction.West && last != Direction.East) {
								directions.addLast(Direction.East);
							}
						}
					}
					break;
				
			
				case KeyEvent.VK_P:
					if(!isGameOver) { // se o jogo n�o terminou
						isPaused = !isPaused;
						logicTimer.setPaused(isPaused); // atualize a pausa do tempo logicTimer
					}
					break;
				
				//	Reinicie o jogo se n�o estiver em andamento.
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
	
	// Inicia o jogo em execu��o.
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

		//Este � o loop do jogo. Ele atualizar� e renderizar� o jogo e continuar� a ser executado at� que a janela do jogo seja fechada.
		while(true) {
			//Obtenha a hora de in�cio do quadro atual.
			long start = System.nanoTime();
			
			//Atualize o temporizador l�gico.
			logicTimer.update();
			
			//se um ciclo tiver decorrido no temporizador l�gico, atualize o jogo.
			if(logicTimer.hasElapsedCycle()) {
				updateGame();
			}
			
			//Repinte a placa e o painel lateral com o novo conte�do.
			board.repaint();
			side.repaint();
			
			// calculo do tempo inicial do inicio do quadro at� o excedente
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
		
		// Obt�m o tipo de ladrilho com o qual a cabe�a da cobra colidiu. Se
		//a cobra bater em uma parede, SnakeBody ser� devolvido
		//  pois ambas as condi��es s�o tratados de forma id�ntica.
		
		TileType collision = updateSnake();
		
		//Aqui lidamos com as diferentes colis�es poss�veis.
		
		//Fruta: Se colidimos com uma fruta, incrementamos o n�mero de frutas que comemos, atualize a pontua��o e gere uma nova fruta.
		
		//SnakeBody: Se colidimos com nossa cauda (ou uma parede), sinalizamos isso o jogo acabou e pause o jogo.
		
		// Se nenhuma colis�o ocorreu, simplesmente decrementamos o n�mero de pontos que a pr�xima fruta nos dar� se for alta o suficiente.
		
		// Isso adiciona um	um pouco de habilidade para o jogo, pois coletar frutas mais rapidamente dar uma pontua��o mais alta.
		
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
	//Atualiza a posi��o e o tamanho da cobra.
	private TileType updateSnake() {

		//Aqui n�s espiamos a pr�xima dire��o ao inv�s de pesquis�-la. Enquanto n�o quebra o jogo, pesquisar a dire��o 
		//aqui causa um pequeno bug	 onde a dire��o da cobra mudar� ap�s o fim do jogo (embora	 n�o vai se mover).
		Direction direction = directions.peekFirst();
				
		// Aqui calculamos o novo ponto em que a cabe�a da cobra estar� ap�s a atualiza��o.	
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
		//pois ambos os casos s�o tratados identicamente.
		
		if(head.x < 0 || head.x >= BoardPanel.COL_COUNT || head.y < 0 || head.y >= BoardPanel.ROW_COUNT) {
			return TileType.SnakeBody; //Pretend we collided with our body.
		}
		
		
		//Aqui obtemos o ladrilho que estava localizado na nova posi��o da cabe�a e	remova a cauda da cobra e a t�bua 
		//se a cobra estiver tempo suficiente, e o ladrilho para o qual se moveu n�o � uma fruta.
		
		//Se a cauda foi removida, precisamos recuperar o ladrilho antigo novamente
		//caso o ladrilho que batemos fosse o peda�o de cauda que acabou de ser removido para evitar um falso game over.
		
		TileType old = board.getTile(head.x, head.y);
		if(old != TileType.Fruit && snake.size() > MIN_SNAKE_LENGTH) {
			Point tail = snake.removeLast();
			board.setTile(tail, null);
			old = board.getTile(head.x, head.y);
		}
		
		
		//Atualize a posi��o da cobra no tabuleiro se n�o colidimos com ela	nossa cauda:
		
		// 	1. Defina a posi��o antiga da cabe�a para um bloco de corpo.
		// 2. Adicione a nova cabe�a � cobra.
		//3. Defina a nova posi��o do cabe�ote para um bloco de cabe�ote.
		
		// Se mais de uma dire��o estiver na fila, fa�a uma sondagem para ler novas entrada.
		
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
		 Redefina as estat�sticas de pontua��o. (Observe que nextFruitPoints � redefinido em a fun��o spawnFruit mais tarde).
		 */
		this.score = 0;
		this.fruitsEaten = 0;
		
		
		/*
		 * Redefina as novas bandeiras de jogo e game over.
		 */
		this.isNewGame = false;
		this.isGameOver = false;
		
		/*
		 * Crie a cabe�a no centro do tabuleiro.
		 */
		Point head = new Point(BoardPanel.COL_COUNT / 2, BoardPanel.ROW_COUNT / 2);

		/*
		 * Limpe a lista de cobras e adicione a cabe�a.
		 */
		snake.clear();
		snake.add(head);
		
		/*
		 * Limpe o tabuleiro e adicione a cabe�a.
		 */
		board.clearBoard();
		board.setTile(head, TileType.SnakeHead);
		
		
		/*
		 * Limpe as dire��es e adicione o norte como odire��o padr�o.
		 */
		directions.clear();
		directions.add(Direction.North);
		
		/*
		 * Reinicialize o temporizador l�gico.
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
	 *Obt�m o sinalizador que indica se estamos jogando um novo jogo ou n�o.
	 */
	public boolean isNewGame() {
		return isNewGame;
	}
	
	/**
	 * Obt�m o sinalizador que indica se o jogo acabou ou n�o.
	 */
	public boolean isGameOver() {
		return isGameOver;
	}
	
	/**
	 * Obt�m o sinalizador que indica se o jogo est� pausado ou n�o.
	 */
	public boolean isPaused() {
		return isPaused;
	}
	
	/**
	 * Gera uma nova fruta no tabuleiro.
	 */
	private void spawnFruit() {
		//configurei pontua��o de 10 em 10.
		this.nextFruitScore = 10;
		
		

		/*
		 * Obtenha um �ndice aleat�rio com base no n�mero de espa�os livres deixados no tabuleiro.
		 */
		int index = random.nextInt(BoardPanel.COL_COUNT * BoardPanel.ROW_COUNT - snake.size());
		
		/*
		 * Embora pud�ssemos facilmente escolher um �ndice aleat�rio no quadroe verifique se est� livre at� encontrarmos um vazio, 
		 * esse m�todo tende a travar se a cobra ficar muito grande.
		 * 
		 * Este m�todo simplesmente faz um loop at� encontrar o en�simo �ndice livre e seleciona usa isso.
		 *  Isso significa que o jogo ser� capaz de localizar um �ndice a uma taxa relativamente constante, independentemente do
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
	 * Obt�m a pontua��o atual.
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * Obt�m o n�mero de frutas ingeridas.
	 */
	public int getFruitsEaten() {
		return fruitsEaten;
	}
	
	/**
	 * Obt�m a pr�xima pontua��o de frutas.
	 */
	public int getNextFruitScore() {
		return nextFruitScore;
	}
	
	public int getNextFruitScoreOne() {
		return nextFruitScoreOne;
	}
	/**
	 * Obt�m a dire��o atual da cobra.
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
