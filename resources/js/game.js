class Config {}
Config.WIDTH = 900
Config.HEIGHT= 360
Config.DEBUG = false
Config.ANTIALIAS = false
Config.ASSETS = 'assets/'

class Game extends Phaser.Game {
    constructor() {
        super(Config.WIDTH, Config.HEIGHT, Phaser.CANVAS,
            'game-container', null, false, Config.ANTIALIAS)

        this.state.add('Play', PlayState, false)
        this.state.start('Play')
    }
}

// Fase 1
class PlayState extends Phaser.State {
    preload() {
        let dir = Config.ASSETS
        // mapa
        this.game.load.tilemap('level1', `${dir}level1.json`, 
            null, Phaser.Tilemap.TILED_JSON);
        this.game.load.image('super_mario',`${dir}super_mario.png`);
    
        this.game.load.spritesheet('mario',`${dir}mario2.png`, 16, 24);

        this.game.load.image('background',`${dir}background3.png`);

        this.game.load.spritesheet('coin',`${dir}coin2.png`, 16, 16);
        
        //this.game.load.image('trophy',`${dir}trophy-200x64.png`);
    }

    createPlayer() {
        this.player = new Player(this.game, this.keys, 80, 100, 'mario')
        this.game.add.existing(this.player)
        
        // camera seca
        //this.game.camera.follow(this.player)

        // camera suave: 0.1 para X e Y eh o fator de interpolacao do deslocamento
        // da camera -> quanto maior, mais rapido ela segue o jogador
        this.game.camera.follow(this.player, Phaser.Camera.FOLLOW_LOCKON, 0.1, 0.1); 
    }

    createMap() {
        // chave para o arquivo .json carregado no metodo preload()
        this.map = this.game.add.tilemap('level1')
        // chave para o arquivo .png de tileset carregado no metodo preload()
        // corresponde ao nome usado para o tileset dentro do Tiled Editor
        this.map.addTilesetImage('super_mario')

        // deve ter o mesmo nome usado na camada criada no Tiled Editor
        this.mapLayer = this.map.createLayer('Tile Layer 1')
        // os indices sao os mesmos para o tiles no Tiled Editor, acrescidos em 1
//        this.map.setCollisionBetween(40, 40, true, 'Tile Layer 1')
        this.map.setCollision([14,15,16, 22, 23, 24, 40], true, 'Tile Layer 1')
        this.mapLayer.resizeWorld()        

        // para cada nova camada: criar camada e definir tiles com colisao
        this.trapsLayer = this.map.createLayer('Traps')
        //this.trapsLayer = this.map.createLayer('Coins')
        this.map.setCollision([14], true, 'Traps')        
    }

    createCoins() {
        this.coins = this.game.add.group()
        // 11 eh o indice do tile
        this.map.createFromObjects('Object Layer 1', 45, 'coin',
                        0, true, false, this.coins, Coin)
    }

    cretateHud() {
        this.scoreText = this.game.add.text(16, 16, '', { fontSize: "16px", fill: '#ffffff' });
        this.scoreText.text = "COINS: 0";
        this.scoreText.fixedToCamera = true;        
    }

    addScore(amount) {
        this.score += amount
        this.scoreText.text = "COINS: " + this.score
    }

    create() {
        this.game.physics.startSystem(Phaser.Physics.ARCADE)
        this.game.stage.backgroundColor = '#000000'
        //this.game.renderer.renderSession.roundPixels = true;

        let bg = this.game.add.tileSprite(0, 0, 
            Config.WIDTH, Config.HEIGHT, 'background')
        bg.fixedToCamera = true
        
        this.keys = this.game.input.keyboard.createCursorKeys()
        this.game.physics.arcade.gravity.y = 550
        this.score = 0

        let fullScreenButton = this.game.input.keyboard.addKey(
            Phaser.Keyboard.ONE)
        fullScreenButton.onDown.add(this.toogleFullScreen, this)

        let screenshotButton = this.game.input.keyboard.addKey(
            Phaser.Keyboard.P)
        screenshotButton.onDown.add(this.takeScreenShot, this)

        this.createMap()
        this.createPlayer()
        this.createCoins() // deve ser apos o createMap()
        this.cretateHud()
        //this.trophy = new Trophy(this.game)
        //this.game.add.existing(this.trophy)
    }

    takeScreenShot() {
        // jQuery
        let imgData = this.game.canvas.toDataURL()

        $('#div-screenshot').append(
            `<img src=${imgData} alt='game screenshot' class='screenshot'>`
        )        
    }

    toogleFullScreen() {
        this.game.scale.fullScreenScaleMode = 
            Phaser.ScaleManager.EXACT_FIT;
        if (this.game.scale.isFullScreen)
            this.game.scale.stopFullScreen()
        else
            this.game.scale.startFullScreen(false)
    }

    update() {
        // colisao do player com o mapa
        this.game.physics.arcade.collide(this.player, this.mapLayer)
        // colisao do player com a camada de armadilhas
        this.game.physics.arcade.collide(
            this.player, this.trapsLayer, this.playerDied, null, this)

        // colisao do player com o grupo de moedas
        this.game.physics.arcade.overlap(
            this.player, this.coins, this.collectCoin, null, this)
    }

    collectCoin(player, coin) {
        // destroi permanentemente o objeto
        coin.destroy() 
        // esconde o objeto e desliga colisao (para reuso futuro)
        //coin.kill() 
        this.addScore(coin.points)
        this.sendCoins();
        //this.trophy.show('first death')   
    }

    sendCoins(){
        console.log(this.score)
        
    }

    playerDied() {
        console.log('player died')
        //this.player.position.setTo(50,200)
        this.player.x = 50
        this.player.y = 200
        this.camera.shake(0.02, 200)
    }

    render() {
        if (Config.DEBUG) {
            this.game.debug.body(this.player)
        }
    }
}

const GAME = new Game()
