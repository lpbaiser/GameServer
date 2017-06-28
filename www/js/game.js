class Game extends Phaser.Game {
    constructor() {
        super(Config.WIDTH, Config.HEIGHT, Phaser.CANVAS,
                'game-container', null, false, Config.ANTIALIAS)

        this.state.add('Play', PlayState, false)
        this.state.add('GameOver', GameOver, false)
        this.state.add('YouWin', YouWin, false)
        //this.state.start('Play')
        this.state.add('Title', TitleState, false)
        this.state.start('Title')
    }
}

var dir = Config.ASSETS

// Fase 1
class PlayState extends GameState {
    preload() {
        this.data = {};
        this.qtdeDied = 0;
        this.playerX = 100;
        this.playerY = 200;
        this.playAgain = true;
        //load map
        this.game.load.tilemap('level1', `${dir}level${Config.LEVEL}.json`, null, Phaser.Tilemap.TILED_JSON);
        //load images
        this.game.load.image('super_mario', `${dir}super_mario.png`);
        this.game.load.image('tiles2', `${dir}tiles2.png`);
        this.game.load.image('mario_tileset', `${dir}mario_tileset.png`);
        this.game.load.image('background', `${dir}background3.png`);
        this.game.load.image('bg_map4', `${dir}bg_map4.png`);
        this.game.load.image('trophy', `${dir}trophy-200x64.png`);
        this.game.load.image('trap1', `${dir}trap1.png`);
        //load sprites
        this.game.load.spritesheet('floorMove', `${dir}floor_move.png`, 16, 48);
        this.game.load.spritesheet('questionMark', `${dir}questionMark.png`, 16, 16);
        this.game.load.spritesheet('bullet', `${dir}fire_mario.png`, 8, 8);
        this.game.load.spritesheet('coin', `${dir}coin2.png`, 16, 16);
        this.game.load.spritesheet('mario', `${dir}mario3.png`, 24, 36);
        this.game.load.spritesheet('mush', `${dir}mush.png`, 16, 16);
        this.game.load.spritesheet('enemies', `${dir}enemies2.png`, 16, 32);
        this.game.load.spritesheet('monsters', `${dir}enemies2.png`, 16, 32);
        this.game.load.spritesheet('flower', `${dir}enemies2.png`, 16, 32);
        this.game.load.spritesheet('check-point', `${dir}check_point_29x32.png`, 29, 32);
        //load sounds
        this.game.load.audio('mario_main', ['assets/audio/main-theme-overworld.mp3']);
        this.game.load.audio('mario-die', ['assets/audio/mario-die.mp3']);
        this.game.load.audio('mario-collect-coin', ['assets/audio/mario-collect-coin.mp3']);
        this.game.load.audio('mario-collect-mush', ['assets/audio/mario-collect-mush.mp3']);
        this.game.load.audio('mario-jump', ['assets/audio/mario-jump.mp3']);
        this.game.load.audio('mario-save-point', ['assets/audio/mario-save-point.mp3']);

       
    }

    createPlayer() {
        this.player = new Player(this.game, this.keys, this.playerX, this.playerY, 'mario', this.weapon)
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
        this.map.addTilesetImage('mario_tileset')
        this.map.addTilesetImage('trap1')

        // deve ter o mesmo nome usado na camada criada no Tiled Editor
        this.mapLayer = this.map.createLayer('TileMap')
        // os indices sao os mesmos para o tiles no Tiled Editor, acrescidos em 1
//        this.map.setCollisionBetween(40, 40, true, 'TileMap')
        this.map.setCollision([15, 16, 22, 23, 40, 27, 28, 426, 499, 500, 501, 601, 429, 448, 467, 423], true, 'TileMap')
        this.mapLayer.resizeWorld()


        // para cada nova camada: criar camada e definir tiles com colisao
        this.trapsLayer = this.map.createLayer('Traps')
        this.map.setCollision([14, 58, 76, 189, 199, 645], true, 'Traps')

        //this.CheckpointsLayer = this.map.createLayer('Checkpoints')
        //this.map.setCollision([10], true, 'Checkpoints')
    }

    createCoins() {
        this.coins = this.game.add.group();
        // 11 eh o indice do tile
        this.map.createFromObjects('Coins', 45, 'coin', 0, true, false, this.coins, Coin);
    }

    createMush() {
        this.mushs = this.game.add.group();
        this.map.createFromObjects('Mush', 207, 'mush', 0, true, false, this.mushs, Mush);
        //this.mushs.forEach((mushs) => mushs.start())
    }

    createQuestionMarkBlock() {
        this.questionMarks = this.game.add.group();
        this.map.createFromObjects('QuestionMark', 413, 'questionMark', 0, true, false, this.questionMarks, QuestionMark);
    }

    createEnemies() {
        this.enemies = this.game.add.group();
        //this.map.setCollision(15, true, 'Enemies')
        this.map.createFromObjects('Enemies', 208, 'enemies', 0, true, false, this.enemies, Enemy);
        this.enemies.forEach((enemies) => enemies.start());
    }

    createFlower() {
        this.flowers = this.game.add.group();
        this.map.createFromObjects('Enemies', 220, 'flower', 0, true, false, this.flowers, Flower);
        this.flowers.forEach((flowers) => flowers.start())
    }

    createFloorMoove() {
        this.floors = this.game.add.group();
        this.map.createFromObjects('FloorMove', 643, 'floorMove', 0, true, false, this.floors, FloorMove);
        //this.map.setCollision([643], true, 'FloorMove')
        this.floors.forEach((floors) => floors.start())
    }

    createTurtleFlyer() {
        this.turtlesFlyer = this.game.add.group();
        this.map.createFromObjects('Enemies', 216, 'enemies', 0, true, false, this.turtlesFlyer, TurtleFly);
        this.turtlesFlyer.forEach((turtlesFlyer) => turtlesFlyer.start())
    }

    createTurtle() {
        this.turtles = this.game.add.group();
        this.map.createFromObjects('Enemies', 214, 'enemies', 0, true, false, this.turtles, Turtle);
        this.turtles.forEach((turtles) => turtles.start())
    }
    createMonster() {
        this.monsters = this.game.add.group();
        this.map.createFromObjects('Enemies', 228, 'monsters', 0, true, false, this.monsters, Monster);
        this.monsters.forEach((monsters) => monsters.start())
    }

    createCheckPoints() {
        this.checkPoint = this.game.add.group();
        this.map.createFromObjects('Checkpoints', 412, 'check-point', 0, true, false, this.checkPoint, CheckPoint);
    }

    createFireBall() {
        this.weapon = this.game.add.weapon(1, 'bullet');
        this.weapon.bulletKillType = Phaser.Weapon.KILL_WORLD_BOUNDS;
//        this.weapon.bulletAngleOffset = 90;
        this.weapon.bulletSpeed = 450;
        this.weapon.fireRate = 100;
        this.weapon.trackSprite(this.player, 14, 0);
        this.game.physics.arcade.overlap(this.weapon, this.enemies, this.killEnemies, null, this)
        console.log("create weapon");
    }

    changeMarioPlayer() {
        this.game.load.spritesheet('mario', `${dir}mario-yoshi.png`, 26, 39);
    }

    cretateHud() {
        this.coinsText = this.game.add.text(16, 10, '', {fontSize: "16px", fill: '#ffee00'});
        this.coinsText.text = "COINS: 0";
        this.coinsText.fixedToCamera = true;

        this.lifeText = this.game.add.text(100, 10, '', {fontSize: "16px", fill: '#086003'});
        this.lifeText.text = "LIFE: " + this.life;
        this.lifeText.fixedToCamera = true;

        this.muteText = this.game.add.text(790, 10, '', {fontSize: "16px", fill: '#ffffff'});
        this.muteText.text = "SOUND: ON"
        this.muteText.fixedToCamera = true;

        this.xpText = this.game.add.text(170, 10, '', {fontSize: "16px", fill: '#0222bf'});
        this.xpText.text = "XP: " + this.xp;
        this.xpText.fixedToCamera = true;

        this.levelText = this.game.add.text(230, 10, '', {fontSize: "16px", fill: '#07f2ff'});
        this.levelText.text = "LEVEL: " + Config.LEVEL;
        this.levelText.fixedToCamera = true;

        this.pauseText = this.game.add.text(420, 154, '', {fontSize: "32px", fill: '#e50909'});
        this.pauseText.text = ""
        this.pauseText.fixedToCamera = true;
    }

    setDeath() {
        this.life -= 1
        this.lifeText.text = "LIFE: " + this.life
        if (this.life == 0) {
            this.gameOver();
        }
    }

    gameOver() {
        this.game.state.start('GameOver')

    }

    addCoin(amount) {
        if (this.amountCoins === 15) {
            this.trophy.show('collector')
            this.addXp(15);
        }
        this.amountCoins += amount
        this.coinsText.text = "COINS: " + this.amountCoins;
    }

    addXp(amount) {
        this.xp += amount;
        this.xpText.text = "XP: " + this.xp
    }

    addLife(amount) {
        this.life += amount;
        Config.LIFE = this.life; 
        this.lifeText.text = "LIFE: " + this.life
        if (this.life === 5) {
            this.trophy.show('cheio vida')
        }
    }

    create() {

        super.create();
        this.game.physics.startSystem(Phaser.Physics.ARCADE)
        this.game.stage.backgroundColor = '#000000'
        //this.game.renderer.renderSession.roundPixels = true;
        let bg;
        if (Config.LEVEL == 2) {
            Config.PULO = -350;
            
        }
        if (Config.LEVEL == 3) {
            Config.PULO = -250;
            bg = this.game.add.tileSprite(0, 0, Config.WIDTH, Config.HEIGHT, 'bg_map4')

        } else {
            bg = this.game.add.tileSprite(0, 0, Config.WIDTH, Config.HEIGHT, 'background')
        }
        bg.fixedToCamera = true

        this.keys = this.game.input.keyboard.createCursorKeys();
        this.game.physics.arcade.gravity.y = 550;
        this.xp = Config.XP;
        this.amountCoins = Config.COINS;
        this.life = Config.LIFE;

        this.music = this.game.add.audio('mario_main');
        this.music.play();
        let fullScreenButton = this.game.input.keyboard.addKey(Phaser.Keyboard.ONE)
        fullScreenButton.onDown.add(this.toogleFullScreen, this)

        let screenshotButton = this.game.input.keyboard.addKey(Phaser.Keyboard.P)
        screenshotButton.onDown.add(this.takeScreenShot, this)

        let pauseButton = this.game.input.keyboard.addKey(Phaser.Keyboard.SPACEBAR)
        pauseButton.onDown.add(this.pauseGame, this)

        let muteButton = this.game.input.keyboard.addKey(Phaser.Keyboard.M)
        muteButton.onDown.add(this.muteGame, this)




        this.createMap()
        this.createQuestionMarkBlock()
        this.createFlower() // deve ser apos o createMap()
        this.pipeLayer = this.map.createLayer('Pipe');
        this.map.setCollision([21, 22, 27, 28], true, 'Pipe')
        this.createTurtleFlyer()// deve ser apos o createMap()
        this.createTurtle()// deve ser apos o createMap()
        this.createMonster()// deve ser apos o createMap()
        this.createFireBall()
        this.createEnemies() // deve ser apos o createMap()
        this.createPlayer()
        this.createCoins() // deve ser apos o createMap()
        this.createMush() // deve ser apos o createMap()
        this.createCheckPoints() // deve ser apos o createMap()
        this.cretateHud()
        this.createFloorMoove()
        if (Config.LEVEL == 2) {
        }

        this.trophy = new Trophy(this.game)
        this.game.add.existing(this.trophy)
        this.game.camera.flash(0x000000, 1000)
        // ao passar sobre o tile da bandeira -> troca de level
        this.map.setTileIndexCallback(39, this.loadNextLevel, this)
        this.map.setTileIndexCallback(468, this.loadNextLevel, this)
        this.levelCleared = false
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
            this.pauseText.text = "PAUSE"
        } else {
            this.pauseText.text = ""
            this.game.paused = false;
        }
    }

    muteGame() {
        if (this.music.mute) {
            this.music.mute = false;
            this.muteText.text = "SOUND: ON"
        } else {
            this.muteText.text = "SOUND: OFF"
            this.music.mute = true;
        }
    }

    update() {
// colisao do player com o mapa
        this.game.physics.arcade.collide(this.player, this.mapLayer)
        this.game.physics.arcade.collide(this.player, this.pipeLayer)
        this.game.physics.arcade.collide(this.player, this.floors)
        // colisao do player com a camada de armadilhas
        this.game.physics.arcade.collide(this.player, this.trapsLayer, this.playerDied, null, this)

        //this.game.physics.arcade.collide(this.mapLayer, this.enemie);
        //  this.game.physics.arcade.collide(this.player, this.enemies, this.killEnemies, null, this)
        this.game.physics.arcade.overlap(this.player, this.enemies, this.touchEnemie, null, this)
        this.game.physics.arcade.overlap(this.player, this.turtlesFlyer, this.touchEnemie, null, this)
        this.game.physics.arcade.overlap(this.player, this.turtles, this.touchEnemie, null, this)
        this.game.physics.arcade.overlap(this.player, this.flowers, this.playerDied, null, this)
        this.game.physics.arcade.overlap(this.player, this.monsters, this.playerDied, null, this)


        // colisao do player com o grupo de moedas
        this.game.physics.arcade.overlap(this.player, this.coins, this.collectCoin, null, this)

        // colisao do player com o cogumelo
        this.game.physics.arcade.collide(this.player, this.questionMarks, this.questionMarkCollide, null, this)
        this.game.physics.arcade.overlap(this.player, this.mushs, this.collectMush, null, this)
        //this.game.physics.arcade.overlap(this.player, this.questionMark, this.questionMarkCollide, null, this)

        this.game.physics.arcade.collide(this.player, this.endPhase, this.finalyPhase, null, this)

        // colisao do player com o checkpoint
        this.game.physics.arcade.collide(this.player, this.checkPoint, this.collectCheckPoint, null, this)

        this.game.physics.arcade.overlap(this.enemies, this.weapon, this.killEnemies, null, this)
        //this.game.physics.arcade.collide(this.balas, this.mapLayer, this.destroiBala, null, this);
        //game.physics.arcade.overlap(this.weapon, this.enemies, this.hitEnemy, null, this);
    }

    collectCoin(player, coin) {

        let music_collect = this.game.add.audio('mario-collect-coin');
        music_collect.play();
        // destroi permanentemente o objeto
        coin.destroy()
        // esconde o objeto e desliga colisao (para reuso futuro)
        //coin.kill() 
        this.addCoin(coin.points)
        //this.trophy.show('first death')   
    }

    touchEnemies(player, enemies) {

        enemies.destroy()
        this.playerDied()
    }

    destroiBala(bala) {
        bala.kill();
    }

    killEnemies(balas, enemies) {
        console.log("tocou")
        enemies.kill()
        balas.destroy()
    }

    collectMush(player, mush) {
        if (!(this.player.body.velocity.y < 0)) {

            let music_collect = this.game.add.audio('mario-collect-mush');
            console.log("entrou")
            music_collect.play();
            // destroi permanentemente o objeto
            mush.destroy()

            this.changeMarioPlayer()
            // esconde o objeto e desliga colisao (para reuso futuro)
            //coin.kill() 
            this.addLife(mush.points)
            //this.trophy.show('first death')   
        } else {
            mush => mush.start();
        }

    }

    questionMarkCollide(player, questionMark) {
        if (this.player.body.velocity.y < 0) {
            //this.mushs.forEach((mushs) => mushs.start())
            questionMark.destroy();
            console.log("Question Mark")
            //this.player.body.velocity.y = -200;
            return;
        } else {
            console.log("collide error")
        }
    }

    collectCheckPoint(player, checkPoint) {
        this.savePoint = this.game.add.audio('mario-save-point');
        this.savePoint.play();
        this.addXp(50);
        checkPoint.destroy();
        this.saveCheckpoint();
    }

    saveCheckpoint() {
        this.playerX = this.player.x;
        this.playerY = this.player.y;

        let data = {
            coins: this.amountCoins,
            life: this.life,
            xp: this.xp,
            save_point_x: this.playerX,
            save_point_y: this.playerY,
            save_point_id: 1
        };

        //console.log(data);


//        this.sendCoins();
        //console.log(this.playerX)
        //console.log(this.playerY)
        ServerComm.addSavePoint(data, response => this.onServerResponse(response));
    }

    onServerResponse(response) {
        console.log(response)
        if (response['response'] != '200') {
            console.log("ERRO de comunicao com o servidor")
            return
        }
        console.log("OK")
    }

    touchEnemie(player, enemie) {
        if (this.player.body.velocity.y > 0) { // kill enemies when hero is falling
            //enemie.kill();
            enemie.enemieKill();
            this.player.body.velocity.y = -200;
            return;
        }
        this.playerDied(player, enemie);
        //this.player.body.velocity.y = -200;
        //this.player.animations.play('dead');
        //this.game.paused = true;

    }

    playerDied(player, enemie) {
        this.music.stop();
        let music_die = this.game.add.audio('mario-die');
        music_die.play();
        this.qtdeDied += 1;
        if (this.qtdeDied === 1) {
            this.addXp(10);
            this.trophy.show('first death')
            console.log("Ganhou um trofeu de primeira morte")
        } else if (this.qtdeDied === 3) {
            if (this.xp < 5) {
                this.xp = 0;
            } else {
                this.xp -= 5;
            }
            this.addXp(0);

            console.log("Ganhou um trofeu 'o noob'")
            this.trophy.show('noob')
        }
        this.setDeath();
        this.player.x = this.playerX
        this.player.y = this.playerY
        setTimeout(this.setPlayMusic.bind(this), 2000);
        this.camera.shake(0.02, 200);
        this.enemies.destroy()
        this.createEnemies()
    }

    setPlayMusic() {
        this.music.play();
    }

    loadNextLevel() {
        if (!this.levelCleared) {
            this.levelCleared = true
            this.game.camera.fade(0x000000, 1000)
            this.game.camera.onFadeComplete.add(this.changeLevel, this)
        }
    }

    changeLevel() {
        Config.LEVEL += 1
        Config.COINS = this.amountCoins
        Config.LIFE = this.life
        Config.XP = this.xp
        this.game.camera.onFadeComplete.removeAll(this)// bug
        this.saveCheckpoint();
        if (Config.LEVEL <= 3)
            this.game.state.restart()
        else
            this.game.state.start('YouWin')
    }

    render() {
        if (Config.DEBUG) {
            this.game.debug.body(this.player)
        }
    }
}

window.onload = function () {
// funciona como singleton
    const GAME = new Game()
}
