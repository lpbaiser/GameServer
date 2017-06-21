
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

        //this.animations.add('spin', [0,1,2,3,4,5], 10, true)
        //this.animations.play('spin')
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

/*class Enemy extends Phaser.Sprite {
 constructor(game, x, y, asset) {
 super(game, x, y, asset)
 this.game.physics.arcade.enable(this)
 this.body.allowGravity = true
 this.body.setSize(0, 18, 5, 13)
 this.autoCull = true
 this.points = 1
 this.body.velocity.x = -50;
 
 this.animations.add('spin', [0, 1], 5, true)
 this.animations.play('spin')
 
 this.body.collideWorldBounds = true;
 //	this.body.bounce.setTo(1, 1);
 this.game.physics.enable(game, Phaser.Physics.ARCADE);
 }
 }*/
class Enemy extends Phaser.Sprite {
    constructor(game, x, y, asset) {
        super(game, x, y, asset)
        this.game.physics.arcade.enable(this)
        this.body.allowGravity = true
        this.body.setSize(0, 18, 5, 13)
        this.autoCull = true
        this.points = 1
        this.body.immovable = true // kinematic

        //this.scale.setTo(1.3, 1.3)
        this.animations.add('move', [0, 1], 5, true);
        this.animations.play('move');
        this.body.collideWorldBounds = true;
        this.game.physics.enable(game, Phaser.Physics.ARCADE);
    }

    start() {
// correcao do problema de ancora do TILED
        this.targetY -= this.height

        let tweenA = this.game.add.tween(this)
                .to({x: this.targetX, y: this.targetY}, 3000, "Quart.easeInOut")
                .to({x: this.x, y: this.y}, 3000, "Quart.easeInOut")
                .loop(-1)
                .start()
    }
}


class Flower extends Phaser.Sprite {
    constructor(game, x, y, asset) {
        super(game, x, y, asset)
        this.game.physics.arcade.enable(this)
        this.body.allowGravity = true
        this.body.setSize(16, 16, 5, 13)
        this.autoCull = true
        this.body.immovable = true // kinematic

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

        let tweenA = this.game.add.tween(this)
                .to({x: this.targetX, y: this.targetY}, 3000, "Quart.easeInOut")
                .to({x: this.x, y: this.y}, 3000, "Quart.easeInOut")
                .loop(1)
                .start()
    }
}
