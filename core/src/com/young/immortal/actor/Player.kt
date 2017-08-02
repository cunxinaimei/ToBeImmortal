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

    enum class Status{
        RUNNING, STANDING
    }

    val col = 8
    val row = 8

    var stateTime = 0f

    var status:Status = Status.STANDING
    var direction: Direction = Direction.Down
    lateinit var runningAnimations: ArrayList<Animation<TextureRegion>>
    lateinit var standingAnimations: ArrayList<Animation<TextureRegion>>

    constructor(camera: OrthographicCamera, runningTexture: Texture, standingTexture: Texture) : this(camera) {
        setSize(200f, 200f)
        runningAnimations = ArrayList(row)
        standingAnimations = ArrayList(row)
        val runningTextureRegion = TextureRegion(runningTexture)
        val trsRunning: Array<Array<TextureRegion>> = runningTextureRegion.split(200, 200)
        for (i in 0..row - 1) {
            val array: com.badlogic.gdx.utils.Array<TextureRegion> = com.badlogic.gdx.utils.Array(8)
            for (j in 0..col - 1) {
                array.add(trsRunning[i][j])
            }
            val anim: Animation<TextureRegion> = Animation(0.1f, array, Animation.PlayMode.LOOP)
            runningAnimations.add(anim)
        }
        val standingTextureRegion = TextureRegion(standingTexture)
        val trsStanding: Array<Array<TextureRegion>> = standingTextureRegion.split(200, 200)
        for (i in 0..row - 1) {
            val array: com.badlogic.gdx.utils.Array<TextureRegion> = com.badlogic.gdx.utils.Array(16)
            for (j in 0..col - 1) {
                array.add(trsStanding[i][j])
            }
            for (j in col - 1..0) {
                array.add(trsStanding[i][j])
            }
            val anim: Animation<TextureRegion> = Animation(0.22f, array, Animation.PlayMode.LOOP)
            standingAnimations.add(anim)
        }
    }

    public fun stand() {
        status = Status.STANDING
        stateTime = 0f
    }

    public fun run(direction: Direction) {
        status = Status.RUNNING
        stateTime = 0f
        this.direction = direction
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        stateTime += Gdx.graphics.deltaTime
        var animations = when(status){
            Status.STANDING->standingAnimations
            Status.RUNNING->runningAnimations
        }
        val anim = animations[direction.index]
//        println("Draw: $originX - $originY")
        batch?.draw(anim.getKeyFrame(stateTime, true), x, y)
    }

}