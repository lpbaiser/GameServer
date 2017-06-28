
class Player extends Phaser.Sprite {
    constructor(game, cursors, x, y, asset, weapon) {
        super(game, x, y, asset, weapon)
        this.keys = cursors
        this.game.physics.enable(this, Phaser.Physics.ARCADE)
        this.body.collideWorldBounds = true
        this.body.setSize(11, 23, 5, 13)
        this.anchor.setTo(0.5, 0.5)
        
        this.weapon = weapon;

        this.animations.add('walk', [2, 1, 2, 3], 10, true)
        this.animations.add('jump', [3], 10, true)
        this.animations.add('idle', [0], 10, true)
        this.animations.add('dead', [4], 10, true)

        this.game.load.audio('mario-jump', ['assets/audio/mario-jump.mp3']);


        this.jumpMax = 1;
        this.jumpCount = 0;
        let jumpButton = this.game.input.keyboard.addKey(Phaser.Keyboard.UP)
        jumpButton.onDown.add(this.jump, this)

        let fireButton = this.game.input.keyboard.addKey(Phaser.Keyboard.F)
        fireButton.onDown.add(this.fire, this)


    }

    jump() {
        this.soundJump = this.game.add.audio('mario-jump');
        if (this.jumpCount < this.jumpMax) {
            this.soundJump.play();
            this.body.velocity.y = Config.PULO;
        }
        this.jumpCount++;
        if (this.body.onFloor()) {
            this.jumpCount = 0;
        }
    }

    fire() {
        let bala = new FireBall(this.game, this.x, this.y +5, 'bullet')
        this.game.add.existing(bala);
        bala.body.velocity.x = 400
        //this.weapon.add(bala);
    }

    update() {
        this.body.velocity.x = 0

        if (this.keys.left.isDown) {
            this.body.velocity.x = -120
        } else if (this.keys.right.isDown) {
            this.body.velocity.x = 120
        }

        let runButton = this.game.input.keyboard.addKey(Phaser.Keyboard.SHIFT)
        if (runButton.isDown) {
            this.body.velocity.x *= 1.7
        }


        this.animate()
    }

    animate() {
        // andando ou parado
        if (this.body.velocity.x != 0)
            this.animations.play('walk')
        else
            this.animations.play('idle')

        // no ar
        if (this.body.velocity.y != 0)
            this.animations.play('jump')

        // define lado
        if (this.body.velocity.x > 0)
            this.scale.x = 1
        else
        if (this.body.velocity.x < 0)
            this.scale.x = -1
    }

}