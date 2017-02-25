# simple_xbee_api_java
[[W.I.P]] using this to generate byte arrays of frames ready to transmit via Xbees on a serial port or such in API mode

>I would love contributions to this project guidelines follow

Functions only
Keep it abstract and modular should be easy for integration into exisitng code
Must keep it simple and easy to understand

Xbee_tx_request - static function, copy into your main class or whichever class you wish to call from.
written only for sending frames not for rx.

remember to read from serial buffer after sending frame if using ACK enabled frame ID
will upload RX frame decoder function once im done with it
