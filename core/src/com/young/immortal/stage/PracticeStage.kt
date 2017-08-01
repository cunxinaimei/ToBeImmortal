package com.young.immortal.stage

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.badlogic.gdx.utils.viewport.Viewport
import com.young.immortal.actor.Player
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.lang.Exception
import java.util.concurrent.TimeUnit


public enum class Direction(val index: Int) {
    Down(0), Left(1), Right(2), Up(3), LeftDown(4), RightDown(5), LeftUp(6), RightUp(7);
}


class PracticeStage(viewport: Viewport) : Stage(viewport) {

    val camera: OrthographicCamera = OrthographicCamera(viewport.worldWidth, viewport.worldHeight)
    var tiledMapRenderer: OrthogonalTiledMapRenderer
    var player: Player = Player(camera, Texture("running.png"))

    var cameraX = 0f
    var cameraY = 0f
    var playerX = 400f
    var playerY = 500f

    val mapWidth:Int
    val mapHeight:Int

    val btnLeft: ImageButton = ImageButton(SpriteDrawable(Sprite(Texture("ic.png"))))
    val btnDown: ImageButton = ImageButton(SpriteDrawable(Sprite(Texture("ic.png"))))
    val btnRight: ImageButton = ImageButton(SpriteDrawable(Sprite(Texture("ic.png"))))
    val btnUp: ImageButton = ImageButton(SpriteDrawable(Sprite(Texture("ic.png"))))

    var isMoving = false

    init {
        addActor(player)
        addActor(btnLeft)
        addActor(btnDown)
        addActor(btnRight)
        addActor(btnUp)

        btnLeft.setPosition(100f, 200f)
        btnDown.setPosition(200f, 100f)
        btnRight.setPosition(300f, 200f)
        btnUp.setPosition(200f, 300f)

        player.setPosition(playerX, playerY)
        val tiledMap = TmxMapLoader().load("des2.tmx")
        val w:Int = tiledMap.properties["width"] as Int
        val tileW:Int = tiledMap.properties["tilewidth"] as Int
        mapWidth = w * tileW
        val h:Int = tiledMap.properties["height"] as Int
        val tileH:Int = tiledMap.properties["tileheight"] as Int
        mapHeight = h * tileH
        tiledMapRenderer = OrthogonalTiledMapRenderer(tiledMap)
        camera.setToOrtho(false)
//        camera.position.x = player.x
//        camera.position.y = player.y
        camera.update()
        cameraX = camera.position.x
        cameraY = camera.position.y
        tiledMapRenderer.setView(camera)
        tiledMapRenderer.render()
        println("${tiledMapRenderer.viewBounds.width} ******** ${tiledMapRenderer.viewBounds.height}")

        btnLeft.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                isMoving = true
                run(Direction.Left)
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                isMoving = false
            }
        })
        btnDown.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                isMoving = true
                run(Direction.Down)
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                isMoving = false
            }
        })
        btnRight.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                isMoving = true
                run(Direction.Right)
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                isMoving = false
            }
        })
        btnUp.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                isMoving = true
                run(Direction.Up)
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                isMoving = false
            }
        })
    }

    override fun act() {
//        var offsetX = player.x - playerX
//        var offsetY = player.y - playerY
//        camera.position.x = cameraX+offsetX
//        camera.position.y = cameraY+offsetY
        camera.update()
        tiledMapRenderer.setView(camera)
        tiledMapRenderer.render()
        super.act()

//        println("${tiledMapRenderer.viewBounds.width} ******** ${tiledMapRenderer.viewBounds.width}")
//        println("Camera Info: ${camera.position.x} -- ${camera.position.y} ** ${camera.viewportWidth} -- ${camera.viewportHeight}")
    }

    fun run(direction: Direction) {
        player.run(direction)
        getRunObservable(direction)
                .repeatUntil{!isPlayerMoving()}
                .subscribe(
                        { },
                        { println(it.message)}
                )
    }

    var offsetX:Float = 0f
    var offsetY:Float = 0f
    fun getRunObservable(direction: Direction): Observable<Vector3> {
        val run:Observable<Vector3> = when (direction) {
            Direction.Left -> Observable.create({
                offsetX = -1f
                if (camera.position.x<=viewport.worldWidth/2){
                    offsetX = 0f
                }
                println("${camera.position.x}--${camera.position.y}")
                camera.position.add(offsetX, 0f, 0f)
                it.onNext(camera.position)
                it.onComplete()
            })
            Direction.Down -> Observable.create({
                offsetY = -1f
                if (camera.position.y<=viewport.worldHeight/2){
                    offsetY = 0f
                }
                println("${camera.position.x}--${camera.position.y}")
                camera.position.add(0f, offsetY, 0f)
                it.onNext(camera.position)
                it.onComplete()
            })
            Direction.Right -> Observable.create({
                offsetX = 1f
                if (camera.position.x+viewport.worldWidth/2>=mapWidth){
                    offsetX = 0f
                }
                println("${camera.position.x}--${camera.position.y}")
                camera.position.add(offsetX, 0f, 0f)
                it.onNext(camera.position)
                it.onComplete()
            })
            Direction.Up -> Observable.create({
                offsetY = 1f
                if (camera.position.y+viewport.worldHeight/2>=mapHeight){
                    offsetY = 0f
                }
                println("${camera.position.x}--${camera.position.y}")
                camera.position.add(0f, offsetY, 0f)
                it.onNext(camera.position)
                it.onComplete()
            })
            else -> Observable.create({
                camera.position.add(0f, 0f, 0f)
                it.onNext(camera.position)
                it.onComplete()
            })
        }
        return Observable.zip(run, Observable.timer(20, TimeUnit.MILLISECONDS),
                BiFunction{t1: Vector3, t2: Long ->  t1})
    }

    fun isPlayerMoving(): Boolean {
//        println("isPlayerMoving? $isMoving")
        return isMoving
    }
}

class InputEventListener : InputListener() {
    override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
        println("touchUp")
        super.touchUp(event, x, y, pointer, button)
    }

    override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
        println("touchDown")
        return true
    }

    override fun touchDragged(event: InputEvent?, x: Float, y: Float, pointer: Int) {
        println("touchDragged")
        super.touchDragged(event, x, y, pointer)
    }
}