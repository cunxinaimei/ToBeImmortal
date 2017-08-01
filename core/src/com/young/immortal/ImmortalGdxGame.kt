package com.young.immortal

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.young.immortal.actor.Player
import com.young.immortal.stage.PracticeStage

class ImmortalGdxGame : ApplicationAdapter() {
    internal lateinit var practiceStage: PracticeStage

    override fun create() {
        practiceStage = PracticeStage(ScreenViewport())
        Gdx.input.inputProcessor = practiceStage
//        practiceStage.addListener(InputEventListener())
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        practiceStage.act()
        practiceStage.draw()
    }

    override fun dispose() {
        practiceStage.dispose()
    }
}
