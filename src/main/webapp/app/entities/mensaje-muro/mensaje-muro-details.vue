<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="mensajeMuro">
        <h2 class="jh-entity-heading" data-cy="mensajeMuroDetailsHeading">
          <span v-text="t$('microbloggingApp.mensajeMuro.detail.title')"></span> {{ mensajeMuro.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('microbloggingApp.mensajeMuro.mensaje')"></span>
          </dt>
          <dd>
            <span>{{ mensajeMuro.mensaje }}</span>
          </dd>
          <dt>
            <span v-text="t$('microbloggingApp.mensajeMuro.fecha')"></span>
          </dt>
          <dd>
            <span v-if="mensajeMuro.fecha">{{ formatDateLong(mensajeMuro.fecha) }}</span>
          </dd>
          <dt>
            <span v-text="t$('microbloggingApp.mensajeMuro.tags')"></span>
          </dt>
          <dd>
            <span>{{ mensajeMuro.tags }}</span>
          </dd>
          <dt>
            <span v-text="t$('microbloggingApp.mensajeMuro.user')"></span>
          </dt>
          <dd>
            <div v-if="mensajeMuro.user">
              <router-link :to="{ name: 'ApplicationUserView', params: { applicationUserId: mensajeMuro.user.id } }">{{
                mensajeMuro.user.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="mensajeMuro.id"
          :to="{ name: 'MensajeMuroEdit', params: { mensajeMuroId: mensajeMuro.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./mensaje-muro-details.component.ts"></script>
