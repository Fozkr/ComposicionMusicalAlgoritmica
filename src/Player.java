
import javax.sound.midi.*;

/**
 *
 * @author Esquivel Oscar
 * @author Garbanzo Diana
 */
public class Player
{
    Track track;
    Sequence sequence;
    Sequencer sequencer;
    long ticks;
    
    final int NOTEON = 144;
    final int NOTEOFF = 128;

    /**
     * Constructor for objects of class Player
     */
    public Player()
    {
        ticks = 0;
        try
        {
            sequencer = MidiSystem.getSequencer();
            sequence = new Sequence(Sequence.PPQ, 10);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return; 
        }
    }

    /**
     * Dados 60 bpm:
     * 60 bpm / 60 segundos-por-minuto = 1 beat por segundo
     * Resolución = 10
     * 10 ticks por beat
     */
    public void createShortEvent(int type, int note, double duration)
    {
        ShortMessage message = new ShortMessage();
        try
        {
            //Se suma la nueva duración en ticks, y eso da el tick para el siguiente beat
            ticks += duration * sequence.getResolution(); //Resolution: 10
            System.out.println(ticks);
            
            message.setMessage(type, note+60, 64);
            MidiEvent event = new MidiEvent(message, ticks);
            track.add(event);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 
     */
    public void playMusic(String[] notasYduraciones)
    {
        track = sequence.createTrack();
        try
        {
            for(int i=0; i<notasYduraciones.length && notasYduraciones[i]!=null; ++i)
            {
                createShortEvent(NOTEON, Integer.parseInt(notasYduraciones[i]), Double.parseDouble(notasYduraciones[i+1]));
                ++i;
            }
            createShortEvent(NOTEON, 0, 4);
            //createShortEvent(NOTEON, 95, 1);
            //createShortEvent(NOTEON, 93, 0.5);
            //createShortEvent(NOTEON, 91, 0.5);
            //createShortEvent(NOTEON, 93, 0.5);
            //createShortEvent(NOTEON, 91, 1);
            //createShortEvent(NOTEON, 93, 0.5);
            //createShortEvent(NOTEON, 95, 1);
            //createShortEvent(NOTEON, 93, 0.5);
            //createShortEvent(NOTEON, 91, 0.5);
            //createShortEvent(NOTEON, 93, 0.5);
            //createShortEvent(NOTEON, 91, 0.5);
            //createShortEvent(NOTEON, 93, 1);
            //createShortEvent(NOTEON, 0, 4);
        }
        catch (Exception ex)
        {
             ex.printStackTrace();
        }
        try
        {
            sequencer.setSequence(sequence);
        }
        catch (InvalidMidiDataException ex)
        {
            ex.printStackTrace();
        }
        MidiDevice device = sequencer;
        try
        {
            device.open();
        }
        catch (MidiUnavailableException ex)
        {
            ex.printStackTrace();
        }
        sequencer.setTempoInBPM(60);
        //sequencer.setLoopEndPoint(sequencer.getSequence().getTickLength());
        sequencer.start();
        
        while(sequencer.isRunning())
        {
            if(sequencer.getTickPosition() >= sequencer.getSequence().getTickLength()) //se llegó al final
            {
                sequencer.stop();
            }
        }

        if (sequencer != null)
        {
            sequencer.close();
        }
        sequencer = null;
    }
}
