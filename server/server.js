var fs = require('fs');
var http = require('http');
var net = require('net');
var io = require('socket.io');

var server = http.createServer(function(request, response) {
	fs.readFile('conversation.html', function(err, data) {
		response.end(data);
	})
});

var websocket = io(server);

websocket.on('connection', function(socket) {
	var passthrough = net.createConnection(8027);

	socket.on('message', function(message) {
		passthrough.write(message + "\n");
	});

	passthrough.on('data', function(data) {
		socket.emit('message', data.toString());
	});
});

server.listen(8028, '0.0.0.0');