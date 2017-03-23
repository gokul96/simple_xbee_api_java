
import com.pi4j.io.serial.*;
import com.pi4j.util.CommandArgumentParser;
import com.pi4j.util.Console;

import java.io.IOException;
import java.util.*;


public class xbeetxrx
{
	//Final variables 

	static final Serial serial = SerialFactory.createInstance(); //pi4j serial instance creation ###IMPORTANT

	//example of how a xbee 64bit address needs to be passed to frame_send or frame_make
	//static final byte[] r1={(byte)0x00,(byte)0x13,(byte)0xA2,(byte)0x00,(byte)0x41,(byte)0x54,(byte)0xED,(byte)0xC9}; //address of router 1

	public static void xbeetxrx() // constructor for xbee serial opening and listening ###CHANGE TO YOUR NEEDS###
	{
		//opening Serial port
		try 
		{
            // create serial config object
            SerialConfig config = new SerialConfig();

            config.device("/dev/ttyUSB0") 		//Change to your serial port ###CHANGE TO YOUR NEEDS###
                  .baud(Baud._9600)
                  .dataBits(DataBits._8)
                  .parity(Parity.NONE)
                  .stopBits(StopBits._1)
                  .flowControl(FlowControl.NONE);

			serial.open(config); 				//open serial port serial.close() will close it
			
		}
			
		catch(IOException ex) 
		{
            System.out.println(" SERIAL SETUP FAILED" + ex.getMessage());
        }
		
		//adding a serial listener to get data from serial port
		serial.addListener(new SerialDataEventListener() //function comes in as a parameter O.o fujava
		{
            @Override
            public void dataReceived(SerialDataEvent event) 
			{

                try 
				{ 
					//System.out.println(event.gethexstring()) //gets and prints the ip data as  a hex string
                	packet_parse(event.getBytes()); // calls the parseing function; can't handle millisecond delays yet			
                } 
				
				catch (IOException e) 
				{
                    e.printStackTrace();
                }
            }
        });
	}
		System.out.println();
		for(int l=0;l<a.length;l++)
				System.out.print(a[l]);
		System.out.println();
	}
	
	static void packet_parse(byte[] rec_dat) //accepts the input data and does relevant operations ###CHANGE TO YOUR NEEDS###
	{
		if(rec_dat[0]==(byte)0x7E) // checks if Xbee packet is recieved by checking for delmiter
		{
			/* if (rec_dat[11]==(byte)0xC9)								//checks last byte of address field in xbee frame
			{															//###CHANGE TO YOUR NEEDS###
				System.out.println("data recieved from router 1");		//###CHANGE TO YOUR NEEDS###
				if (rec_dat[15]==(byte)0x03)							//###CHANGE TO YOUR NEEDS###
				{														//###CHANGE TO YOUR NEEDS###
					for(int i=0;i<8;i++)								//###CHANGE TO YOUR NEEDS###
						if((rec_dat[17]|(1<<(7-i)))==rec_dat[17])		// Application Specific code change as per your needs
							a[i]=1;										// here rec_dat[15] is the first byte of recived data
																		// i use it to ID what kind of packet i got for application
						printar(a);										//###CHANGE TO YOUR NEEDS###
				}														//###CHANGE TO YOUR NEEDS###
				if (rec_dat[11]==(byte)0x24)							//###CHANGE TO YOUR NEEDS###
				{														//###CHANGE TO YOUR NEEDS###
					System.out.println("data recieved from router 2")	//###CHANGE TO YOUR NEEDS###
				}														//###CHANGE TO YOUR NEEDS###			
				if (rec_dat[11]==(byte)0x2F)							//###CHANGE TO YOUR NEEDS### 
				{														//###CHANGE TO YOUR NEEDS###	
					System.out.println("data recieved from router 3")	//###CHANGE TO YOUR NEEDS###
				}														//###CHANGE TO YOUR NEEDS###
			} */
		}
	}

	static byte[] frame_make(byte[] address, byte data[]) //makes the API mode frame for the Xbee transmit request type
	{
		int s=18+data.length;
		byte[] frame= new byte[s];
		int i=0;
		int j=0;
		byte len=(byte)0x0E;
		len=(byte)(len+data.length);

		frame[i++]=(byte)0x7E; 		//frame Delimiter
		frame[i++]=(byte)0x00; 		//frame length high (not used)
		frame[i++]=(byte)len;		//frame length low
		frame[i++]=(byte)0x10;		//frame type 0x10 - Tx req 64bit address
		frame[i++]=(byte)0x00;		//frame ID (oxo1-disable retry, 0x00 - disable ACK)
		
					
		for (j=0; j<8; j++)
			frame[i++]=address[j];	//insert address fixedlength of 8 bytes
	
		frame[i++]=(byte)0xFF;		//16bit broad cast address high (not used)
		frame[i++]=(byte)0xFE;		//16bit broadcast address low	(not used)
		frame[i++]=(byte)0x00;		//broadcast radius 0x00 - max
		frame[i++]=(byte)0x00;		//options
			
		for (j=0;j<data.length;j++)
			frame[i++]=data[j];	//insert data
	
		byte cs=(byte)0x00;		//initalize checksum byte
	
		for(int t=3; t<s; t++)
			cs+=(byte)frame[t];	//calulate checksum add all bytes
	
		cs = (byte)((-1)-cs);		//checksum = 0xFF-cs, 0xFF is -1 as a signed int
		frame[i]=cs;			//put frame checksum in its place
		return frame;			//return frame
	}
	
	static int send_byte(byte [] abc) //writes given byte array to serial
	{    
		try 
		{
			serial.write((byte[]) abc);		//writes to serial port by using pi4j command
			return 1;
        }
                
		catch(IOException ex)
		{
            ex.printStackTrace();
			return -1;
        }
    }
		dat[0]=(byte)0x02;
		dat[1]=(byte)0x01;
		byte[] abc=frame_make(r1,dat);
		if(send_byte(abc)!=1)
				System.out.println("failure");
	}

	static public int sendframe(byte[] address, byte[] data) //call this function to send data
	{	
		if(send_byte(frame_make(address,data)))
			return 1;
		else 
			return -1;
	}
}

			
            	