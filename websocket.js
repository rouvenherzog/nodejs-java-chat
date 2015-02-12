var net = require('net');

var messages = [];
var Message = function( text, author ) {
	this.text = text;
	this.author = author;

	this.toJson = function() {
		return {
			text: this.text,
			author: this.author
		};
	};

	this.toString = function() {
		return JSON.stringify(this.toJson()) + "\n";
	};
};

var listeners = [];

var server = net.createServer(function(connection) {
	var author = connection.remoteAddress;

	console.log(author, "CONNECTED");

	var receive_message = function( messages ) {
		var message = [];
		for( var index in messages )
			message.push(messages[index].toJson());

		console.log(author, "SEND MESSAGE", JSON.stringify(message));
		connection.write(JSON.stringify(message) + "\n");
	};

	var broadcast_message = function( data ) {
		var message = new Message(
			data.toString(undefined, 0, data.length-1),
			author
		);

		messages.push(message);
		console.log("================================================");
		console.log(author, "NOTIFY", listeners.length, "LISTENERS");
		for( var index in listeners )
			listeners[index]([message]);
	};

	var add_connection = function() {
		listeners.push( receive_message );
		receive_message(messages);
	};

	var remove_connection = function() {
		console.log(author, "DISCONNECTED");
		if( listeners.indexOf(receive_message) != -1 )
			listeners.splice(listeners.indexOf(receive_message), 1);
	};

	connection.on('end', remove_connection);
	connection.on('error', remove_connection);
	connection.on('data', broadcast_message);

	add_connection();
});


server.listen(8027, '0.0.0.0', function() {
	console.log("===========================");
	console.log("Server started on " + server.address().address  + ":" + server.address().port);
	console.log("Max " + server.maxConnections + " connections allowed.");
	console.log("===========================");
});