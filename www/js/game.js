class Config {
}
Config.WIDTH = 900
Config.HEIGHT = 340
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

var dir = Config.ASSETS

// Fase 1
class PlayState extends Phaser.State {
    preload() {
        this.data = {};
        this.qtdeDied = 0;
        this.playerX = 50;
        this.playerY = 200;
        this.playAgain = true;
        //load map
        this.game.load.tilemap('level1', `${dir}level1.json`, null, Phaser.Tilemap.TILED_JSON);
        //load images
        this.game.load.image('super_mario', `${dir}super_mario.png`);
        this.game.load.image('tiles2', `${dir}tiles2.png`);
        this.game.load.image('background', `${dir}background3.png`);
        this.game.load.image('trophy', `${dir}trophy-200x64.png`);
        //load sprites
        this.game.load.spritesheet('mario', `${dir}mario2.png`, 24, 36);
        this.game.load.spritesheet('coin', `${dir}coin2.png`, 16, 16);
        this.game.load.spritesheet('mush', `${dir}mush.png`, 16, 16);
        this.game.load.spritesheet('enemies', `${dir}enemies2.png`, 16, 32);
        this.game.load.spritesheet('flowers', `${dir}enemies2.png`, 16, 32);
        //load sounds
        this.game.load.audio('mario_main', ['assets/audio/main-theme-overworld.mp3']);
        this.game.load.audio('mario-die', ['assets/audio/mario-die.mp3']);
        this.game.load.audio('mario-collect-coin', ['assets/audio/mario-collect-coin.mp3']);
        this.game.load.audio('mario-collect-coin', ['assets/audio/mario-collect-mush.mp3']);
    }

    createPlayer() {
        this.player = new Player(this.game, this.keys, this.playerX, this.playerY, 'mario')
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
        this.map.addTilesetImage('tiles2')

        // deve ter o mesmo nome usado na camada criada no Tiled Editor
        this.mapLayer = this.map.createLayer('Tile Layer 1')
        // os indices sao os mesmos para o tiles no Tiled Editor, acrescidos em 1
//        this.map.setCollisionBetween(40, 40, true, 'Tile Layer 1')
        this.map.setCollision([14, 15, 16, 22, 23, 24, 40, 27, 28], true, 'Tile Layer 1')
        this.mapLayer.resizeWorld()

        // para cada nova camada: criar camada e definir tiles com colisao
        this.trapsLayer = this.map.createLayer('Traps')
        this.map.setCollision(14, true, 'Traps')
        this.map.setCollision(58, true, 'Traps')
        this.map.setCollision(76, true, 'Traps')
        this.map.setCollision(189, true, 'Traps')
        this.map.setCollision(199, true, 'Traps')
        this.CheckpointsLayer = this.map.createLayer('Checkpoints')
        this.map.setCollision([10], true, 'Checkpoints')
    }

    createCoins() {
        this.coins = this.game.add.group();
        // 11 eh o indice do tile
        this.map.createFromObjects('Object Layer 1', 45, 'coin', 0, true, false, this.coins, Coin);
    }

    createMush() {
        this.mush = this.game.add.group();
        this.map.createFromObjects('Mush Layer', 207, 'mush', 0, true, false, this.mush, Mush);
    }

    createEnemies() {
        this.enemies = this.game.add.group();
        //this.map.setCollision(15, true, 'Enemies Layer')
        this.map.createFromObjects('Enemies Layer', 208, 'enemies', 0, true, false, this.enemies, Enemy);
    }

    createFlower() {
        this.flower = this.game.add.group();
        this.map.createFromObjects('Enemies Layer', 220, 'flowers', 0, true, false, this.flower, Flower);
    }

    changeMarioPlayer() {
        this.game.load.spritesheet('mario', `${dir}mario-yoshi.png`, 26, 39);
    }

    /*createFinalyPhase(){
     this.endPhase = this.game.add.group();
     this.map.createFromObjects('Object Layer 1', 39, 'super_mario',
     0, true, false, this.endPhase, null);
     }*/

    cretateHud() {
        this.scoreText = this.game.add.text(16, 16, '', {fontSize: "16px", fill: '#ffffff'});
        this.scoreText.text = "COINS: 0";
        this.scoreText.fixedToCamera = true;
    }

    cretateHudLife() {
        this.lifeText = this.game.add.text(100, 16, '', {fontSize: "16px", fill: '#ffffff'});
        this.lifeText.text = "LIFE: " + this.life
        this.lifeText.fixedToCamera = true;
    }

    setDeath() {
        if (this.life === 1) {
            this.gameOver();
            return;
        }
        this.life -= 1
        this.lifeText.text = "LIFE: " + this.life
    }

    gameOver() {
        if (this.playAgain === true) {
            this.playerX = 50;
            this.playerY = 200;
            this.create();
        } else {
            console.log("Tela para login")
        }
    }

    addScore(amount) {
        this.score += amount
        this.scoreText.text = "COINS: " + this.score
    }

    addLife(amount) {
        this.life += amount
        this.lifeText.text = "LIFE: " + this.life
    }

    create() {
        this.game.physics.startSystem(Phaser.Physics.ARCADE)
        this.game.stage.backgroundColor = '#000000'
        //this.game.renderer.renderSession.roundPixels = true;

        let bg = this.game.add.tileSprite(0, 0, Config.WIDTH, Config.HEIGHT, 'background')
        bg.fixedToCamera = true

        this.keys = this.game.input.keyboard.createCursorKeys()
        this.game.physics.arcade.gravity.y = 550
        this.score = 0
        this.life = 3

        this.music = this.game.add.audio('mario_main');
        this.music.play();

        let fullScreenButton = this.game.input.keyboard.addKey(Phaser.Keyboard.ONE)
        fullScreenButton.onDown.add(this.toogleFullScreen, this)

        let screenshotButton = this.game.input.keyboard.addKey(Phaser.Keyboard.P)
        screenshotButton.onDown.add(this.takeScreenShot, this)

        let pauseButton = this.game.input.keyboard.addKey(Phaser.Keyboard.SPACEBAR)
        pauseButton.onDown.add(this.pauseGame, this)

        this.createMap()
        this.createPlayer()
        this.createCoins() // deve ser apos o createMap()
        this.createEnemies() // deve ser apos o createMap()
        this.createFlower() // deve ser apos o createMap()
        this.createMush() // deve ser apos o createMap()
        this.cretateHud()
        this.cretateHudLife()
        //this.createFinalyPhase()
        this.trophy = new Trophy(this.game)
        this.game.add.existing(this.trophy)
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

    pauseGame() {
        if (this.game.paused === false) {
            this.game.paused = true;
        } else {
            this.game.paused = false;
        }
    }

    update() {
        // colisao do player com o mapa
        this.game.physics.arcade.collide(this.player, this.mapLayer)
        // colisao do player com a camada de armadilhas
        this.game.physics.arcade.collide(this.player, this.trapsLayer, this.playerDied, null, this)

        this.game.physics.arcade.collide(this.mapLayer, this.enemies);
        this.game.physics.arcade.collide(this.mapLayer, this.flower);

        //  this.game.physics.arcade.collide(this.player, this.enemies, this.killEnemies, null, this)
        this.game.physics.arcade.overlap(this.player, this.enemies, this.touchEnemies, null, this)
        this.game.physics.arcade.overlap(this.player, this.flower, this.playerDied, null, this)


        // colisao do player com o grupo de moedas
        this.game.physics.arcade.overlap(this.player, this.coins, this.collectCoin, null, this)

        // colisao do player com o cogumelo
        this.game.physics.arcade.overlap(this.player, this.mush, this.collectMush, null, this)

        this.game.physics.arcade.collide(this.player, this.endPhase, this.finalyPhase, null, this)

        // colisao do player com o checkpoint
        this.game.physics.arcade.collide(this.player, this.CheckpointsLayer, this.saveCheckpoint, null, this)
    }

    collectCoin(player, coin) {

        let music_collect = this.game.add.audio('mario-collect-coin');
        music_collect.play();

        // destroi permanentemente o objeto
        coin.destroy()
        // esconde o objeto e desliga colisao (para reuso futuro)
        //coin.kill() 
        this.addScore(coin.points)
        //this.trophy.show('first death')   
    }

    touchEnemies(player, enemies) {

        enemies.destroy()
        this.playerDied()
    }

    killEnemies(player, enemies) {
        console.log("tocou")
        enemies.destroy()
    }

    collectMush(player, mush) {
        let music_collect = this.game.add.audio('mario-collect-mush');
        music_collect.play();
        // destroi permanentemente o objeto
        mush.destroy()

        this.changeMarioPlayer()
        // esconde o objeto e desliga colisao (para reuso futuro)
        //coin.kill() 
        this.addLife(mush.points)
        //this.trophy.show('first death')   
    }

    /*finalyPhase(player, endPhase){
     console.log("ACABOU")
     endPhase.destroy()
     }*/

    saveCheckpoint() {
//        this.sendCoins();
        this.playerX = this.player.x;
        this.playerY = this.player.y;
        console.log(this.playerX)
        console.log(this.playerY)
    }

    sendCoins() {
        console.log(this.score)
        ServerComm.addScore({score: this.score}, (response) => this.onServerResponse(response))
    }

    onServerResponse(response) {
        console.log(response)
        if (response['response'] != '200') {
            console.log("ERRO de comunicao com o servidor")
            return
        }
        console.log("OK")
    }

    playerDied() {
        this.music.stop();
        let music_die = this.game.add.audio('mario-die');
        music_die.play();

        this.qtdeDied += 1;
        if (this.qtdeDied === 1) {
            this.trophy.show('first death')
            console.log("Ganhou um trofeu de primeira morte")
        } else if (this.qtdeDied === 10) {
            console.log("Ganhou um trofeu 'o morredor'")
        }
        this.setDeath();
        //this.player.position.setTo(50,200)
//        this.player.x = 50
//        this.player.y = 200
        this.player.x = this.playerX
        this.player.y = this.playerY
        setTimeout(this.setPlayMusic.bind(this), 2000);

        this.camera.shake(0.001, 300)
        this.enemies.destroy()
        this.createEnemies()
    }

    setPlayMusic() {
        this.music.play();
    }

    render() {
        if (Config.DEBUG) {
            this.game.debug.body(this.player)
        }
    }
}

var GAME = new Game()
