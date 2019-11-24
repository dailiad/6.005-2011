package piano;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.sound.midi.MidiUnavailableException;

import midi.Midi;
import music.Pitch;

import org.junit.Test;

public class PianoMachineTest {
	
	PianoMachine pm = new PianoMachine();
	
    @Test
    public void singleNoteTest() throws MidiUnavailableException {
        String expected0 = "on(61,PIANO) wait(100) off(61,PIANO)";
        
    	Midi midi = Midi.getInstance();

    	midi.clearHistory();
    	
        pm.beginNote(new Pitch(1));
		Midi.wait(100);
		pm.endNote(new Pitch(1));

        System.out.println(midi.history());
        assertEquals(expected0,midi.history());
    }
    
    @Test
    public void multipleNotesTest() throws MidiUnavailableException {
        String expected0 = "on(61,PIANO) wait(100) off(61,PIANO) wait(100) "
                         + "on(62,PIANO) wait(100) off(62,PIANO) wait(100) "
                         + "on(71,PIANO) wait(100) off(71,PIANO)";
        
        Midi midi = Midi.getInstance();

        midi.clearHistory();
        
        pm.beginNote(new Pitch(1));
        Midi.wait(100);
        pm.endNote(new Pitch(1));
        Midi.wait(100);
        pm.beginNote(new Pitch(2));
        Midi.wait(100);
        pm.endNote(new Pitch(2));
        Midi.wait(100);
        pm.beginNote(new Pitch(11));
        Midi.wait(100);
        pm.endNote(new Pitch(11));

        System.out.println(midi.history());
        assertEquals(expected0,midi.history());
    }

}
