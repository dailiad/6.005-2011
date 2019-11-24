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
        assertEquals(expected0, midi.history());
    }

    @Test
    public void multipleNotesTest() throws MidiUnavailableException {
        String expected0 = "on(61,PIANO) wait(100) off(61,PIANO) wait(100) "
                + "on(62,PIANO) wait(100) off(62,PIANO) wait(100) " + "on(71,PIANO) wait(100) off(71,PIANO)";

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
        assertEquals(expected0, midi.history());
    }

    // test changeInstrument()
    @Test
    public void testChangeOnce() throws MidiUnavailableException {
        String expected0 = "on(61,PIANO) wait(100) off(61,PIANO) wait(100) "
                + "on(62,BRIGHT_PIANO) wait(100) off(62,BRIGHT_PIANO)";

        Midi midi = Midi.getInstance();

        midi.clearHistory();

        pm.beginNote(new Pitch(1));
        Midi.wait(100);
        pm.endNote(new Pitch(1));
        Midi.wait(100);
        pm.changeInstrument();
        pm.beginNote(new Pitch(2));
        Midi.wait(100);
        pm.endNote(new Pitch(2));

        System.out.println(midi.history());
        assertEquals(expected0, midi.history());
    }

    @Test
    public void testChangeMoreThanOnce() throws MidiUnavailableException {

        String expected0 = "on(61,PIANO) wait(100) off(61,PIANO) wait(100) "
                + "on(62,FRENCH_HORN) wait(100) off(62,FRENCH_HORN)";

        Midi midi = Midi.getInstance();

        midi.clearHistory();

        pm.beginNote(new Pitch(1));
        Midi.wait(100);
        pm.endNote(new Pitch(1));
        Midi.wait(100);
        // change 60 times
        for (int i = 0; i < 60; i++) {
            pm.changeInstrument();
        }
        pm.beginNote(new Pitch(2));
        Midi.wait(100);
        pm.endNote(new Pitch(2));

        System.out.println(midi.history());
        assertEquals(expected0, midi.history());
    }

    @Test
    public void testChangeOver128() throws MidiUnavailableException {

        String expected0 = "on(61,PIANO) wait(100) off(61,PIANO) wait(100) "
                + "on(62,ELECTRIC_GRAND) wait(100) off(62,ELECTRIC_GRAND)";

        Midi midi = Midi.getInstance();

        midi.clearHistory();

        pm.beginNote(new Pitch(1));
        Midi.wait(100);
        pm.endNote(new Pitch(1));
        Midi.wait(100);
        // change 130 times
        for (int i = 0; i < 130; i++) {
            pm.changeInstrument();
        }
        pm.beginNote(new Pitch(2));
        Midi.wait(100);
        pm.endNote(new Pitch(2));

        System.out.println(midi.history());
        assertEquals(expected0, midi.history());
    }

    // test shift pitches strategy:
    // shift path: shiftLevel 0 -> 1 -> 2 -> 3 -> 0 - > -1 -> -2 -> -3
    // pitches: first, last
    
    @Test
    public void testShiftUpAndDown() throws MidiUnavailableException {
        Midi midi = Midi.getInstance();
        
        // initial state : pitch level = 0
        String expected0 = "on(60,PIANO) wait(100) off(60,PIANO) wait(0) "
                + "on(71,PIANO) wait(100) off(71,PIANO)";
        
        
        
        midi.clearHistory();
        
        pm.beginNote(new Pitch(0));
        Midi.wait(100);
        pm.endNote(new Pitch(0));
        pm.beginNote(new Pitch(11));
        Midi.wait(100);
        pm.endNote(new Pitch(11));

        System.out.println(midi.history());
        assertEquals(expected0, midi.history());
        
        // shift up 1 : pitch level = 1
        String expected1 = "on(72,PIANO) wait(100) off(72,PIANO) wait(0) "
                + "on(83,PIANO) wait(100) off(83,PIANO)";
        
        midi.clearHistory();
        
        pm.shiftUp();
        
        pm.beginNote(new Pitch(0));
        Midi.wait(100);
        pm.endNote(new Pitch(0));
        pm.beginNote(new Pitch(11));
        Midi.wait(100);
        pm.endNote(new Pitch(11));

        System.out.println(midi.history());
        assertEquals(expected1, midi.history());
        
        // shift up 2 : pitch level = 2
        String expected2 = "on(84,PIANO) wait(100) off(84,PIANO) wait(0) "
                + "on(95,PIANO) wait(100) off(95,PIANO)";
        
        midi.clearHistory();
        
        pm.shiftUp();
        
        pm.beginNote(new Pitch(0));
        Midi.wait(100);
        pm.endNote(new Pitch(0));
        pm.beginNote(new Pitch(11));
        Midi.wait(100);
        pm.endNote(new Pitch(11));

        System.out.println(midi.history());
        assertEquals(expected2, midi.history());
        
     // shift up 3 and should stay the same: pitch level = 2
        midi.clearHistory();
        
        pm.shiftUp();
        
        pm.beginNote(new Pitch(0));
        Midi.wait(100);
        pm.endNote(new Pitch(0));
        pm.beginNote(new Pitch(11));
        Midi.wait(100);
        pm.endNote(new Pitch(11));

        System.out.println(midi.history());
        assertEquals(expected2, midi.history());
        
        // shift down 1 : pitch level = 1
        midi.clearHistory();
        
        pm.shiftDown();
        
        pm.beginNote(new Pitch(0));
        Midi.wait(100);
        pm.endNote(new Pitch(0));
        pm.beginNote(new Pitch(11));
        Midi.wait(100);
        pm.endNote(new Pitch(11));

        System.out.println(midi.history());
        assertEquals(expected1, midi.history());
        
        // shift down 3 : pitch level = -2
        String expected3 = "on(36,PIANO) wait(100) off(36,PIANO) wait(0) "
                + "on(47,PIANO) wait(100) off(47,PIANO)";
        midi.clearHistory();
        
        pm.shiftDown();
        pm.shiftDown();
        pm.shiftDown();
        
        pm.beginNote(new Pitch(0));
        Midi.wait(100);
        pm.endNote(new Pitch(0));
        pm.beginNote(new Pitch(11));
        Midi.wait(100);
        pm.endNote(new Pitch(11));

        System.out.println(midi.history());
        assertEquals(expected3, midi.history());
        
        // shift down 1 again should stay the same
        midi.clearHistory();
        
        pm.shiftDown();
        
        pm.beginNote(new Pitch(0));
        Midi.wait(100);
        pm.endNote(new Pitch(0));
        pm.beginNote(new Pitch(11));
        Midi.wait(100);
        pm.endNote(new Pitch(11));

        System.out.println(midi.history());
        assertEquals(expected3, midi.history());
    }
    
    
    
}
