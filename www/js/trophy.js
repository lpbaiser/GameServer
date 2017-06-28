class Trophy extends Phaser.Sprite {
    constructor(game) {
        super(game, 0, 0, '')

        this.data = {}
        this.data['first death'] =
                {name: 'first death', xp: 10,
                    title: 'KEEP CALM AND PLAY',
                    description: 'First death on game'}

        this.data['collector'] =
                {name: 'noob', xp: 15,
                    title: 'THE MAN COLLECTOR',
                    description: 'You collected 15 coins'}
        this.data['noob'] =
                {name: 'noob', xp: 0,
                    title: 'YOU ARE A NOOB',
                    description: 'Third death on game'}
        this.data['cheio vida'] =
                {name: 'vida5', xp: 0,
                    title: 'CHEIO DE VIDA',
                    description: 'Conseguiu chegar a 5 vidas'}

        this.panels = [] // fila de paineis de trofeus
        this.achieved = [] // lista dos nomes do trofeus jah conquistados

        //ServerComm.clearTrophy((r) => console.log( JSON.stringify(r) ) ) 

        // listar os trofeus no servidor e atualizar this.achieved
        ServerComm.listTrophy((response) => this.updateAchievedTrophies(response));
    }

    updateAchievedTrophies(json) {
        // coloca os nomes dos trofeus na lista de controle: this.achieved
        let list = json['data']
        for (let t of list) {
            this.achieved.push(t['name'])
            this.addTrophyOnPage(t['name'])
        }
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
        label.text += this.data[trophyName].xp + '\n\n'
        label.text += this.data[trophyName].description

        return panel
    }

    show(trophyName) {
        let panel = this.createPanel(trophyName)
        this.panels.push(panel)

        this.game.time.events.add(Phaser.Timer.SECOND * 3, this.removePanel, this)

        ServerComm.addTrophy(this.data[trophyName], (response) => this.onServerResponse(response, trophyName));
    }

    onServerResponse(response, trophyName) {
        if (response['response'] != '200') {
            console.log("ERRO de comunicao com o servidor")
            return
        }
        console.log("status: " + response['response'])
        this.achieved.push(trophyName)

        //let panel = this.createPanel(trophyName)
        //this.panels.push(panel)
        // agenda a destruicao do panel
        //this.game.time.events.add(Phaser.Timer.SECOND * 3, this.removePanel, this)

        this.addTrophyOnPage(trophyName)
    }

    addTrophyOnPage(trophyName) {
        /*
         // DOM
         let divTrophy = document.getElementById('div-trophy')
         divTrophy.innerHTML += 
         '<p>' + JSON.stringify(this.data['first death']) + '</p>'
         */
        // jQuery
        //$('#div-trophy').append('<p>' + JSON.stringify(this.data[trophyName]) + '</p>')
        let html = Templates.trophiesListItem(this.data[trophyName])
        $('#div-trophy').append(html)
    }

    removePanel() {
        let p = this.panels.shift()
        p.destroy()
    }

}

class Templates {
}
Templates.trophiesListItem = Handlebars.compile(
        $('#template-trophies-list-item').html()
        );
Templates.profileItem = Handlebars.compile(
        $('#template-profile-item').html()
        );
