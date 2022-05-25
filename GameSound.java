package projeto.snake.game;
import java.io.File;
import javax.sound.sampled.*;

public class GameSound {
	
	//cria��o da classe para toocar m�sica durante execu��o do jogo
	void Song() { //M�todo GameSong para chamar na classe executavel.
	try {
	//URL do som.cola o som na c:
	AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("C:\\Musicajogo\\som2.wav").getAbsoluteFile());
	Clip clip = AudioSystem.getClip();
	clip.open(audioInputStream);
	clip.start();
	clip.loop(Clip.LOOP_CONTINUOUSLY); //Para repetir o som.
	} catch (Exception ex) {
	System.out.println("Erro ao executar SOM!");
	ex.printStackTrace();
	}
	
}
}
