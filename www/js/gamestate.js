class Config {}
Config.RATIO = 2.77;
Config.HEIGHT = 340;
Config.CONTAINER_HEIGHT = 340;
Config.WIDTH = 900;
Config.CONTAINER_WIDTH  = Config.CONTAINER_HEIGHT * Config.RATIO;
Config.DEBUG = false;
Config.ANTIALIAS = false;
Config.ASSETS = 'assets/';
Config.LEVEL = 1;
Config.SCORE = 0;
Config.DEBUG = false;


class GameState extends Phaser.State {
    create() {
        let fullScreenButton = this.input.keyboard.addKey(Phaser.Keyboard.ONE);
        fullScreenButton.onDown.add(this.toggleFullScreen, this);   

        this.scaleGame();
    }

    toggleFullScreen() {
        this.scale.fullScreenScaleMode = Phaser.ScaleManager.EXACT_FIT;
        if (this.scale.isFullScreen) {
            this.scale.stopFullScreen();
        } else {
            this.scale.startFullScreen(false);
        }
    }

    scaleGame() {
        this.game.scale.scaleMode = Phaser.ScaleManager.USER_SCALE //RESIZE

        // escala da tela        
        this.game.scale.setResizeCallback(function(scale, parentBounds) {
            //this.game.scale.setMaximum()
            let scaleX = Config.CONTAINER_WIDTH / Config.WIDTH
            let scaleY = Config.CONTAINER_HEIGHT / Config.HEIGHT

            this.game.scale.setUserScale(scaleX, scaleY, 0, 0)
        }, this)        
    }
}