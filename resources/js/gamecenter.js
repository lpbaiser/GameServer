
class Trophy extends Phaser.Sprite {
    constructor(game) {
        super(game, 0, 0, '')

        this.data = {}
        this.data['first death'] = 
        {name: 'first death', xp: 10, 
        title: 'KEEP CALM AND PLAY',
        description: 'First death on game'}

        this.panels = []
    }

    createPanel(trophyName) {
       let panelY = this.game.height - 74 - this.panels.length * 74
       let panel = this.game.add.sprite(this.game.width - 250,
                        panelY, 'trophy')
        panel.fixedToCamera = true 
        //panel.alpha = 0

        let labelX = 66
        let labelWidth = panel.width - labelX
        let style = {font: '10px Arial', fill: '#ffffff',
            wordWrap: true, wordWrapWidth: labelWidth}
        let label = this.game.add.text(labelX, 5, '', style)
        label.lineSpacing = -7
        panel.addChild(label)

        // define label
        label.text = this.data[trophyName].title + '   +'
        label.text+= this.data[trophyName].xp + '\n\n'
        label.text+= this.data[trophyName].description

        return panel
    }

    show(trophyName) {
        let panel = this.createPanel(trophyName)
        this.panels.push(panel)
        ServerComm.addTrophy(this.data['first death'],
            (response) => 
                console.log('addTrophy: ' + JSON.stringify(response)))
        // agenda a destruicao do panel
        this.game.time.events.add(Phaser.Timer.SECOND * 3,
            this.removePanel, this)
    }

    removePanel() {
        let p = this.panels.shift()
        p.destroy()
    }
}

class ServerComm {
    static addTrophy(data, callback) {
        ServerComm.sendRequestTrophy(
            'john_doe', 'add-trophy', data, callback)
    }

    // metodo generico a ser usado por todas as requisicoes de trofeus
    static sendRequestTrophy(user, opName, opData, callback) {
        let data = {
            id: user,
            op: opName,
            data: opData
        }
        ServerComm.ajaxPost(data, callback)
    }

    static ajaxPost(data, callback) {
        let url = 'http://localhost:8000'
        $.post(url, JSON.stringify(data))
            .done(function(data, status) {
                let jsonObj = JSON.parse(data)
                callback(jsonObj)
            })
            .fail(function(jqXHR, status, errorThrown) {
                console.log('ERROR: cannot reach game server')
            })
    }
}