import com.pi4j.io.serial.*;
import com.pi4j.util.CommandArgumentParser;
import com.pi4j.util.Console;

import java.io.IOException;
import java.util.Date;

//Copied parts of code from the Pi4j serial sender sample code.


public class ExampleTxReq{



static byte[] frame_make(byte[] address, byte data[])
{
		int s=18+data.length;
		byte[] frame= new byte[s];	//create byte[] array of desired size
		
		int i=0;					//counter
		int j=0;					//counter
		byte len=(byte)0x0E;		//length byte 
		len=(byte)(len+data.length);//Length caluclation type casted to int and back to byte

		frame[i++]=(byte)0x7E; 		//frame Delimiter
		frame[i++]=(byte)0x00; 		//frame length high (not used)
		frame[i++]=(byte)len;		//frame length low
		frame[i++]=(byte)0x10;		//frame type 0x10 - Tx req 64bit address
		frame[i++]=(byte)0x01;		//frame ID
		
					
		for (j=0; j<8; j++)
			frame[i++]=address[j];	//insert address fixedlength of 8 bytes
	
		frame[i++]=(byte)0xFF;		//16bit broad cast address high (not used)
		frame[i++]=(byte)0xFE;		//16bit broadcast address low	(not used)
		frame[i++]=(byte)0x00;		//broadcast radius 0x00 - max
		frame[i++]=(byte)0x00;		//options
			
		for (j=0;j<data.length;j++)
			frame[i++]=data[j];		//insert data
	
		byte cs=(byte)0x00;			//initalize checksum byte
	
		for(int t=3; t<s; t++)
			cs+=(byte)frame[t];		//calulate checksum add all bytes
	
		cs = (byte)((-1)-cs);		//checksum = 0xFF-cs, 0xFF is -1 as a signed int
		frame[i]=cs;				//put frame checksum in its place
		return frame;				//return frame
}



    public static void main(String args[]) throws InterruptedException, IOException 
{

 
        final Console console = new Console();
        console.title("<-- Custom made Frame sender xbee -->","Test");
        console.promptForExit();
        final Serial serial = SerialFactory.createInstance(); //pi4j lib

        serial.addListener(new SerialDataEventListener() {
            @Override
            public void dataReceived(SerialDataEvent event) {

                try {
                    console.println("[HEX DATA]   " + event.getHexByteString());
                    console.println("[ASCII DATA] " + event.getAsciiString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            // create serial config object
            SerialConfig config = new SerialConfig(); //INSERT YOUR XBEE'S Config Data Here

            config.device("/dev/ttyUSB0")
                  .baud(Baud._9600)
                  .dataBits(DataBits._8)
                  .parity(Parity.NONE)
                  .stopBits(StopBits._1)
                  .flowControl(FlowControl.NONE);

            if(args.length > 0){
                config = CommandArgumentParser.getSerialConfig(config, args);
            }
	//input data
	byte[] add={(byte)0x00,(byte)0x13,(byte)0xA2, (byte)0x00,(byte)0x40,(byte)0xD7,(byte)0xBD, (byte)0x24}; // Change to Recipient Xbee's Address (64bit)
	byte[] dat={(byte)0x10,(byte)0x20,(byte)0x22};
	
	byte[] pay=frame_make(add,dat);
        
            
	for(int i=0;i<pay.length;i++)
			System.out.println("G"+pay[i]); //prints in ACSII how do you get it to print in hex ..??



	console.box(" Connecting to: " + config.toString(),
                    " We are sending ASCII data on the serial port every 1 second.",
                    " Data received on serial port will be displayed below."); 
	
           serial.open(config);
		
		
            // continuous loop to keep the program running until the user terminates the program
            while(console.isRunning()) {
                try {

			serial.write((byte[]) pay);
	
		
			//serial.write((byte) 0x30);


                }
                catch(IllegalStateException ex){
                    ex.printStackTrace();
                }

                // wait 1 second before continuing
                Thread.sleep(1000);
            }

        }
        catch(IOException ex) {
            console.println(" ==>> SERIAL SETUP FAILED : " + ex.getMessage());
            return;
        }
    }
}
