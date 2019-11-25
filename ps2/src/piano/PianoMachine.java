package piano;

import javax.sound.midi.MidiUnavailableException;
import java.util.List;
import java.util.ArrayList;

import midi.Instrument;
import midi.Midi;
import music.Pitch;
import music.NoteEvent;
import music.NoteEvent.Kind;

public class PianoMachine {
	
	private Midi midi;
	private Instrument currentInstrument = Midi.DEFAULT_INSTRUMENT;
	// initial pitch level is 0, means the middle pitch.
	// -2 <= pitchlevel <= 2
	private int pitchLevel = 0;
	private boolean isRecording = false;
	
	private List<NoteEvent> rhythm = new ArrayList<>();
    
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
    	midi.beginNote(getMidiFrequency(rawPitch), currentInstrument);
    	if (isRecording) {
    	    recordRhythm(rawPitch, Kind.start);
    	}

    }
    
    /**
     * stop playing a pitch
     * @param rawPitch: the pitch to be stopped
     */
    public void endNote(Pitch rawPitch) {
    	midi.endNote(getMidiFrequency(rawPitch), currentInstrument);
    	if (isRecording) {
    	    recordRhythm(rawPitch, Kind.stop);
    	}
    }
    
    /**
     * get midi frequency from a raw pitch with its level
     * @param rawPitch
     * @return note: pitch with right pitch level
     */
    private int getMidiFrequency(Pitch rawPitch) {
        return rawPitch.toMidiFrequency() + pitchLevel * Pitch.OCTAVE;
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
    
    /**
     * alternate between recording and non-recording mode, a new recording will replace the old one.
     * @return true if in recording mode and false else
     */
    public boolean toggleRecording() {
        if (!isRecording) {
            rhythm = new ArrayList<>();
        }
        isRecording = !isRecording;
    	return isRecording;
    }
    
    /**
     *  add NoteEvent to this.rhythm list
     */
    private void recordRhythm(Pitch pitch, Kind kind) {
        long time = System.currentTimeMillis();
        rhythm.add(new NoteEvent(pitch, time, currentInstrument, kind));
    }
    
    /**
     * play the recorded rhythm
     */
    protected void playback() {
        isRecording = false;
        midi.clearHistory();
        if (rhythm.isEmpty()) {
            return;
        } else
        if (rhythm.size() == 1) {
            NoteEvent noteEvent = rhythm.get(0);
            playNoteEvent(noteEvent);
        } else {
            int duration = 0;
            NoteEvent prevNoteEvent = null;
            NoteEvent currNoteEvent = null;
            for (int i = 0; i < rhythm.size() - 1; i++) {
                prevNoteEvent = rhythm.get(i);
                currNoteEvent = rhythm.get(i+1);
                duration = (int) (currNoteEvent.getTime() - prevNoteEvent.getTime()) / 10;
                playNoteEvent(prevNoteEvent);
                Midi.wait(duration);
                
            }
            playNoteEvent(currNoteEvent);
        }
        
        
    }
    
    /**
     * play a note event
     * @param noteEvent
     */
    private void playNoteEvent(NoteEvent noteEvent) {
        if (noteEvent.getKind() == Kind.start) {
                midi.beginNote(getMidiFrequency(noteEvent.getPitch()), currentInstrument);
            } else {
                midi.endNote(getMidiFrequency(noteEvent.getPitch()), currentInstrument);
            }
    }

}
