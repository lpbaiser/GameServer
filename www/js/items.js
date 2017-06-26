
class Coin extends Phaser.Sprite {
    constructor(game, x, y, asset) {
        super(game, x, y, asset)
        this.game.physics.arcade.enable(this)
        this.body.allowGravity = false
        this.autoCull = true
        this.points = 1

        this.animations.add('spin', [0, 1, 2, 3, 4, 5], 10, true)
        this.animations.play('spin')
    }
}

class Mush extends Phaser.Sprite {
    constructor(game, x, y, asset) {
        super(game, x, y, asset)
        this.game.physics.arcade.enable(this)
        this.body.allowGravity = false
        this.autoCull = true
        this.points = 1
        this.anchor.setTo(0, 0)

        //this.animations.add('spin', [0,1,2,3,4,5], 10, true)
        //this.animations.play('spin')
    }

    start() {
// correcao do problema de ancora do TILED
        this.targetY -= this.height

        let tweenA = this.game.add.tween(this)
                .to({x: this.targetX, y: this.targetY}, 4000, Phaser.Easing.Linear.None)
                //.to({x: this.x, y: this.y}, 4000, Phaser.Easing.Linear.None)
                //.loop(-1)
                .start()
    }
}

class CheckPoint extends Phaser.Sprite {
    constructor(game, x, y, asset) {
        super(game, x, y, asset)
        this.game.physics.arcade.enable(this)
        this.body.allowGravity = false
        this.autoCull = true
        this.points = 1

        //this.animations.add('spin', [0,1,2,3,4,5], 10, true)
        //this.animations.play('spin')
    }
}

class Enemy extends Phaser.Sprite {
    constructor(game, x, y, asset) {
        super(game, x, y, asset)
        this.game.physics.arcade.enable(this)
        this.body.allowGravity = false
        this.autoCull = true
        this.body.setSize(25, 14, 3, 15)
        this.body.immovable = true // kinematic

//        this.scale.setTo(1.3, 1.3)

        this.animations.add('move', [0, 1], 10, true)
        this.animations.add('dead', [2], 10, true)
        this.animations.play('move')
    }

    start() {
        // correcao do problema de ancora do TILED
        this.targetY -= this.height

        this.tweenA = this.game.add.tween(this)
                .to({x: this.targetX, y: this.targetY}, this.time, Phaser.Easing.Linear.None)
                .to({x: this.x, y: this.y}, this.time, Phaser.Easing.Linear.None)
                .loop(-1)
                .start()
    }

    enemieKill() {
        this.animations.play('dead');
        this.tweenA.stop();
        this.body.enable = false;
        this.game.time.events.add(Phaser.Timer.SECOND * 1, this.kill, this);
    }
}

class TurtleFly extends Phaser.Sprite {
    constructor(game, x, y, asset) {
        super(game, x, y, asset)
        this.game.physics.arcade.enable(this)
        this.body.allowGravity = false
        this.autoCull = true
        this.body.setSize(25, 14, 3, 15)
        this.body.immovable = true // kinematic
        this.anchor.setTo(0.5, 0.5)

        this.lastX = this.x;
//        this.scale.setTo(1.3, 1.3)

        this.animations.add('move', [8, 9], 10, true)
        this.animations.add('dead', [10], 10, true)
        this.animations.play('move')
    }

    start() {
        // correcao do problema de ancora do TILED
        this.targetY -= this.height

        this.tweenA = this.game.add.tween(this)
                .to({x: this.targetX, y: this.targetY}, this.time, Phaser.Easing.Linear.None)
                //.to( { angle: 180 }, 0, Phaser.Easing.Linear.None)
                .to({x: this.x, y: this.y}, this.time, Phaser.Easing.Linear.None)
                .loop(-1)
                .start()
    }

    update() {

        if (this.x > this.lastX) {
            this.scale.x = -1;
        } else {
            this.scale.x = 1;
        }
        this.lastX = this.x;
    }

    enemieKill() {
        this.animations.play('dead');
        //this.body.allowGravity = false;
        this.tweenA.stop();
        this.body.enable = false;
        this.game.time.events.add(Phaser.Timer.SECOND * 1, this.kill, this);
    }
}

class Turtle extends Phaser.Sprite {
    constructor(game, x, y, asset) {
        super(game, x, y, asset)
        this.game.physics.arcade.enable(this)
        this.body.allowGravity = false
        this.autoCull = true
        this.body.setSize(25, 14, 3, 15)
        this.body.immovable = true // kinematic
        this.anchor.setTo(0.5, 0)

        this.lastX = this.x;
//        this.scale.setTo(1.3, 1.3)

        this.animations.add('move', [6, 7], 10, true)
        this.animations.add('dead', [10], 10, true)
        this.animations.play('move')
    }

    start() {
        // correcao do problema de ancora do TILED
        this.targetY -= this.height

        this.tweenA = this.game.add.tween(this)
                .to({x: this.targetX, y: this.targetY}, this.time, Phaser.Easing.Linear.None)
                //.to( { angle: 180 }, 0, Phaser.Easing.Linear.None)
                .to({x: this.x, y: this.y}, this.time, Phaser.Easing.Linear.None)
                .loop(-1)
                .start()
    }

    update() {

        if (this.x > this.lastX) {
            this.scale.x = -1;
        } else {
            this.scale.x = 1;
        }
        this.lastX = this.x;
    }

    enemieKill() {
        this.animations.play('dead');
        //this.body.allowGravity = false;
        this.tweenA.stop();
        this.body.enable = false;
        this.game.time.events.add(Phaser.Timer.SECOND * 1, this.kill, this);
    }
}

class Flower extends Phaser.Sprite {
    constructor(game, x, y, asset) {
        super(game, x, y, asset)
        this.game.physics.arcade.enable(this)
        this.body.allowGravity = false
        this.body.setSize(16, 16, 5, 13)
        this.autoCull = true
        this.body.immovable = false // kinematic

//        this.scale.setTo(1.3, 1.3)

        //this.animations.add('spin', [12, 13], 5, true)
        //this.animations.play('spin')

        this.animations.add('move', [12, 13], 5, true)
        this.animations.play('move')



//        this.body.collideWorldBounds = true;
//	this.body.bounce.setTo(1, 1);
//        this.game.physics.enable(game, Phaser.Physics.ARCADE);
    }

    start() {
// correcao do problema de ancora do TILED
        this.targetY -= this.height

        this.tweenA = this.game.add.tween(this)
                .to({x: this.targetX, y: this.targetY}, 4000, Phaser.Easing.Linear.None)
                .to({x: this.x, y: this.y}, 4000, Phaser.Easing.Linear.None)
                .loop(-1)
                .start()
    }

}

class QuestionMark extends Phaser.Sprite {
    constructor(game, x, y, asset) {
        super(game, x, y, asset)
        this.game.physics.arcade.enable(this)
        this.body.allowGravity = false
        this.autoCull = true
        this.points = 1
        this.body.setSize(16, 16, 5, 13)

        //this.animations.add('spin', [0, 1, 2, 3, 4, 5], 10, true)
        //this.animations.play('spin')
    }
}

class FireBall extends Phaser.Sprite {
    constructor(game, x, y, asset) {
        super(game, x, y, asset);
        this.game.physics.arcade.enable(this);
        this.body.allowGravity = false;
        this.autoCull = true;
        this.points = 1;
        this.body.setSize(16, 16, 5, 13)
        this.game.time.events.add(Phaser.Timer.SECOND * 5, this.kill, this);
        this.animations.add('spin', [0, 1, 2, 3], 10, true)
        this.animations.play('spin')
    }
}

class FloorMove extends Phaser.Sprite {
    constructor(game, x, y, asset) {
        super(game, x, y, asset)
        this.game.physics.arcade.enable(this)
        this.body.setSize(50, 16, 1, 0)
        this.autoCull = true
        //this.body.immovable = false // kinematic
        this.body.collideWorldBounds = true;
        this.body.immovable = true;
        this.body.allowGravity = false;

//        this.scale.setTo(1.3, 1.3)

        //this.animations.add('spin', [12, 13], 5, true)
        //this.animations.play('spin')

        //this.animations.add('move', [0], 5, true)
        //this.animations.play('move')



//        this.body.collideWorldBounds = true;
//	this.body.bounce.setTo(1, 1);
//        this.game.physics.enable(game, Phaser.Physics.ARCADE);
    }

    start() {
// correcao do problema de ancora do TILED
        this.targetY -= this.height

        this.tweenA = this.game.add.tween(this)
                .to({x: this.targetX, y: this.targetY}, this.time, Phaser.Easing.Linear.None)
                .to({x: this.x, y: this.y}, this.time, Phaser.Easing.Linear.None)
                .loop(-1)
                .start()
    }

}

class Monster extends Phaser.Sprite {
    constructor(game, x, y, asset) {
        super(game, x, y, asset)
        this.game.physics.arcade.enable(this)
        this.body.allowGravity = false
        this.autoCull = true
        this.body.setSize(25, 14, 3, 15)
        this.body.immovable = true // kinematic
        this.anchor.setTo(0.5, 0)

        this.lastX = this.x;
//        this.scale.setTo(1.3, 1.3)

        this.animations.add('move', [20, 21, 22, 23], 10, true)
        //this.animations.add('dead', [10], 10, true)
        this.animations.play('move')
    }

    start() {
        // correcao do problema de ancora do TILED
        this.targetY -= this.height

        this.tweenA = this.game.add.tween(this)
                .to({x: this.targetX, y: this.targetY}, this.time, Phaser.Easing.Linear.None)
                //.to( { angle: 180 }, 0, Phaser.Easing.Linear.None)
                .to({x: this.x, y: this.y}, this.time, Phaser.Easing.Linear.None)
                .loop(-1)
                .start()
    }

    update() {

        if (this.x > this.lastX) {
            this.scale.x = -1;
        } else {
            this.scale.x = 1;
        }
        this.lastX = this.x;
    }

    enemieKill() {
        this.animations.play('dead');
        //this.body.allowGravity = false;
        this.tweenA.stop();
        this.body.enable = false;
        this.game.time.events.add(Phaser.Timer.SECOND * 1, this.kill, this);
    }
}
