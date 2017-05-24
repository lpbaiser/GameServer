window.onload = getFiles;

var files;

function getFiles() {
	$.get("/files" + window.location.pathname, function(data) {
		files = data;
		createTable();
    });
}

function createTable() {
	table = "";
	table += '<table border="1">';
	table += '<tr>';
	table += '<th>File</th>';
	table += '<th>Last modified</th>';
	table += '<th>Size</th>';
	table += '</tr>';
	for (entry in files) {
		table += '<tr>';
		if (files[entry]["isNavigable"]) {
			if (window.location.pathname != "/") {
				url = window.location.pathname + "/" + entry
			} else {
				url = entry
			}
			table += "<td><a href=" + url + ">" + entry + "</a></td>";
		} else {
			table += "<td>" + entry + "</td>";
		}
		table += "<td>" + files[entry]["lastModified"] + "</td>";
		table += "<td>" + files[entry]["size"] + "</td>";
		table += '</tr>';
	}
	table += '</table>';
	document.getElementById("fileList").innerHTML = table;
}