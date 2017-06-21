class ServerComm {
    static addTrophy(data, callback) {
        ServerComm.sendRequest('lpbaiser', 'add-trophy', data, callback);
    }

    static listTrophy(callback) {
        ServerComm.sendRequest('lpbaiser', 'list-trophy', '', callback);
    }

    static clearTrophy(callback) {
        ServerComm.sendRequest('lpbaiser', 'clear-trophy', '', callback);
    }

    static addScore(data, callback) {
        ServerComm.sendRequest('lpbaiser', 'add-score', data, callback);
    }

    static addSavePoint(data, callback) {
        ServerComm.sendRequest('lpbaiser', 'save-point', data, callback);
    }

    static login(data, callback) {
        //alert("login")
        ServerComm.ajaxPost(data, callback)
    }

    // metodo generico a ser usado por todas as 
    // requisicoes de trofeus
    static sendRequest(user, opName, opData, callback) {
        let data = {
            id: user,
            op: opName,
            data: opData
        }
        ServerComm.ajaxPost(data, callback)
    }

    static ajaxPost(data, callback) {
        let url = 'http://localhost:8000/game'
        $.post(url, JSON.stringify(data))
                .done(function (data, status) {
                    let jsonObj = JSON.parse(data)
                    callback(jsonObj)
                })
                .fail(function (jqXHR, status, errorThrown) {
                    console.log(status)
                    console.log('ERROR: cannot reach game server')
                })
    }
}



