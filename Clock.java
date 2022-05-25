package projeto.snake.game;

// Classe respons�vel em rastrear o numero de cliclos que decorreram ao longo do tempo
public class Clock {
	
	//	O n�mero de milissegundos que comp�em um ciclo.
	private float millisPerCycle;
	
	//A �ltima vez que o rel�gio foi atualizado (usado para calcular o tempo ).
	private long lastUpdate;
	
	//O n�mero de ciclos decorridos e ainda n�o pesquisados
	private int elapsedCycles;
	
	// 	A quantidade de tempo excedente para o pr�ximo ciclo decorrido.
	private float excessCycles;
	
	//Se o rel�gio est� pausado ou n�o.
	private boolean isPaused;
	
	// criado um novo rel�gio que defini os ciclos por segundo 
	public Clock(float cyclesPerSecond) {
		setCyclesPerSecond(cyclesPerSecond);
		reset();
	}
	
	// numero de ciclos decorridos por segundos
	public void setCyclesPerSecond(float cyclesPerSecond) {
		this.millisPerCycle = (1.0f / cyclesPerSecond) * 1500;
	}
	
	 // redefinindo as estatisticas do relogio
	public void reset() {
		this.elapsedCycles = 0; 		//ciclo decorrido = 0
		this.excessCycles = 0.0f;		// ciclo em excesso = 0
		this.lastUpdate = getCurrentTime(); // hora da ultima atualiza��o para a hora atual
		this.isPaused = false;  		// sinalizador pausado como falso
	}
	
		// atualizando as estatisticas do relogio.
	public void update() {
		
		long currUpdate = getCurrentTime();  // sera calculado ciclos decorridos e excesso somente se tiver  n�o estiver pausado
		float delta = (float)(currUpdate - lastUpdate) + excessCycles; 
		
		//Atualiza o n�mero de ticks decorridos e em excesso se n�o estivermos pausados.
		if(!isPaused) {
			this.elapsedCycles += (int)Math.floor(delta / millisPerCycle);
			this.excessCycles = delta % millisPerCycle;
		}
		
		//Definido a hora da �ltima atualiza��o para o pr�ximo ciclo de atualiza��o.
		this.lastUpdate = currUpdate;
	}
	
	// pausa ou retoma o rel�gio.
	public void setPaused(boolean paused) {
		this.isPaused = paused;
	}
	
	//verifica se o rel�gico est� pausado no momento
	public boolean isPaused() {
		return isPaused;
	}
	
	// verifica se o ciclo j� decorreu para este rel�gio
	public boolean hasElapsedCycle() {
		if(elapsedCycles > 0) {
			this.elapsedCycles--; // ent�o o numeros de cilos ser� decrementado em 1
			return true;
		}
		return false;
	}
	
	// cerifica se o ciclo percorreu para este relogio
	public boolean peekElapsedCycle() {
		return (elapsedCycles > 0); // o numero de ciclos n�o ser� reduzido se o numero de ciclos corridos for maior que 0
	}
	
	// hora atual em milissegundos
	private static final long getCurrentTime() {
		return (System.nanoTime() / 1000000L); // velocidade do jogo em milissegundos
	}

}
