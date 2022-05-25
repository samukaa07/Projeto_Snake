package projeto.snake.game;

// Classe responsável em rastrear o numero de cliclos que decorreram ao longo do tempo
public class Clock {
	
	//	O número de milissegundos que compõem um ciclo.
	private float millisPerCycle;
	
	//A última vez que o relógio foi atualizado (usado para calcular o tempo ).
	private long lastUpdate;
	
	//O número de ciclos decorridos e ainda não pesquisados
	private int elapsedCycles;
	
	// 	A quantidade de tempo excedente para o próximo ciclo decorrido.
	private float excessCycles;
	
	//Se o relógio está pausado ou não.
	private boolean isPaused;
	
	// criado um novo relógio que defini os ciclos por segundo 
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
		this.lastUpdate = getCurrentTime(); // hora da ultima atualização para a hora atual
		this.isPaused = false;  		// sinalizador pausado como falso
	}
	
		// atualizando as estatisticas do relogio.
	public void update() {
		
		long currUpdate = getCurrentTime();  // sera calculado ciclos decorridos e excesso somente se tiver  não estiver pausado
		float delta = (float)(currUpdate - lastUpdate) + excessCycles; 
		
		//Atualiza o número de ticks decorridos e em excesso se não estivermos pausados.
		if(!isPaused) {
			this.elapsedCycles += (int)Math.floor(delta / millisPerCycle);
			this.excessCycles = delta % millisPerCycle;
		}
		
		//Definido a hora da última atualização para o próximo ciclo de atualização.
		this.lastUpdate = currUpdate;
	}
	
	// pausa ou retoma o relógio.
	public void setPaused(boolean paused) {
		this.isPaused = paused;
	}
	
	//verifica se o relógico está pausado no momento
	public boolean isPaused() {
		return isPaused;
	}
	
	// verifica se o ciclo já decorreu para este relógio
	public boolean hasElapsedCycle() {
		if(elapsedCycles > 0) {
			this.elapsedCycles--; // então o numeros de cilos será decrementado em 1
			return true;
		}
		return false;
	}
	
	// cerifica se o ciclo percorreu para este relogio
	public boolean peekElapsedCycle() {
		return (elapsedCycles > 0); // o numero de ciclos não será reduzido se o numero de ciclos corridos for maior que 0
	}
	
	// hora atual em milissegundos
	private static final long getCurrentTime() {
		return (System.nanoTime() / 1000000L); // velocidade do jogo em milissegundos
	}

}
