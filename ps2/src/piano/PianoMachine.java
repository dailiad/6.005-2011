package piano;

import javax.sound.midi.MidiUnavailableException;

import midi.Instrument;
import midi.Midi;
import music.Pitch;

public class PianoMachine {
	
	private Midi midi;
	private Instrument currentInstrument = Midi.DEFAULT_INSTRUMENT;
	// initial pitch level is 0, means the middle pitch.
	// -2 <= pitchlevel <= 2
	private int pitchLevel = 0;
    
	/**
	 * constructor for PianoMachine.
	 * 
	 * initialize midi device and any other state that we're storing.
	 */
    public PianoMachine() {
    	try {
            midi = Midi.getInstance();
        } catch (MidiUnavailableException e1) {
            System.err.println("Could not initialize midi device");
            e1.printStackTrace();
            return;
        }
    }
    
    /**
     * play a pitch
     * @param rawPitch : the pitch to be played by midi device
     */
    public void beginNote(Pitch rawPitch) {
    	midi.beginNote(rawPitch.toMidiFrequency() + pitchLevel * Pitch.OCTAVE, currentInstrument);

    }
    
    /**
     * stop playing a pitch
     * @param rawPitch: the pitch to be stopped
     */
    public void endNote(Pitch rawPitch) {
    	midi.endNote(rawPitch.toMidiFrequency() + pitchLevel * Pitch.OCTAVE, currentInstrument);
    }
    
    /**
     * change the current playing instrument to the next instrument in the
     * standard ordering (or the first if this is the last)
     */
    public void changeInstrument() {
        currentInstrument = currentInstrument.next();
    }
    
    /**
     * shift current pitch level up by 1.
     * pitch level will never higher than 2.
     */
    public void shiftUp() {
        if (pitchLevel < 2) {
            pitchLevel++;
        }
    }
    
    
    /**
     * shift current pitch level up by 1.
     * pitch level will never lower than -2.
     */
    public void shiftDown() {
        if (pitchLevel > -2) {
            pitchLevel--;
        }
    }
    
    //TODO write method spec
    public boolean toggleRecording() {
    	return false;
    	//TODO: implement for question 4
    }
    
    //TODO write method spec
    protected void playback() {    	
        //TODO: implement for question 4
    }

}
