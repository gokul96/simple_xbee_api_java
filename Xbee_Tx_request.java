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
} // copy to class you wish to call from
