package com.young.immortal.actor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.young.immortal.stage.Direction

class Player(val camera: OrthographicCamera) : Actor() {

    val col = 8
    val row = 8

    var stateTime = 0f

    var direction: Direction = Direction.Down
    lateinit var animations: ArrayList<Animation<TextureRegion>>

    constructor(camera: OrthographicCamera, texture: Texture) : this(camera) {
        val textureRegion = TextureRegion(texture)
        setSize(200f, 200f)
        animations = ArrayList(row)
        val trs: Array<Array<TextureRegion>> = textureRegion.split(200, 200)
        for (i in 0..row - 1) {
            val array: com.badlogic.gdx.utils.Array<TextureRegion> = com.badlogic.gdx.utils.Array(8)
            for (j in 0..col - 1) {
                array.add(trs[i][j])
            }
            val anim: Animation<TextureRegion> = Animation(0.1f, array, Animation.PlayMode.LOOP)
            animations.add(anim)
        }

        addListener(ClickEventListener())
    }

    public fun tapped() {

        var next = when (direction) {
            Direction.Down -> Direction.Left
            Direction.Left -> Direction.Right
            Direction.Right -> Direction.Up
            Direction.Up -> Direction.LeftDown
            Direction.LeftDown -> Direction.RightDown
            Direction.RightDown -> Direction.LeftUp
            Direction.LeftUp -> Direction.RightUp
            else -> Direction.Down
        }
        run(next)
    }

    public fun run(direction: Direction) {
        stateTime = 0f
        this.direction = direction
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        stateTime += Gdx.graphics.deltaTime
        val anim = animations[direction.index]
//        println("Draw: $originX - $originY")
        batch?.draw(anim.getKeyFrame(stateTime, true), x, y)
    }


    internal class ClickEventListener : ClickListener() {
        override fun clicked(event: InputEvent?, x: Float, y: Float) {
            val player: Player = event?.listenerActor as Player
            player.tapped()
        }
    }

}