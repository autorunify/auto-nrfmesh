<template>
  <ion-page>
    <ion-header :translucent="true">
      <ion-toolbar>
        <ion-title>Blank</ion-title>
      </ion-toolbar>
    </ion-header>

    <ion-content :fullscreen="true">
      <ion-header collapse="condense">
        <ion-toolbar>
          <ion-title size="large">Blank</ion-title>
        </ion-toolbar>
      </ion-header>

      <div id="container">
        <ion-list>
          <ion-item v-for="dev in devicesRef">
            <ion-label>{{ dev.address }}</ion-label>
            <ion-button slot="end" @click="onDeviceBind(dev)" :disabled="dev.provisioned">BIND</ion-button>
          </ion-item>
        </ion-list>

        <ion-button expand="full" @click="onRequestPermissions" style="margin-bottom: 8px;">权限检测</ion-button>
        <ion-button expand="full" @click="onInitMeshNetwork" style="margin-bottom: 8px;">初始化网络</ion-button>
        <ion-button expand="full" @click="onDeviceScanner" style="margin-bottom: 32px;">扫描设备</ion-button>
        <ion-button expand="full" @click="onConnectToProxy" style="margin-bottom: 32px;">连接代理</ion-button>
        <ion-button expand="full" @click="onTestNetwork" style="margin-bottom: 32px;">网络测试</ion-button>

      </div>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { IonContent, IonHeader, IonPage, IonTitle, IonToolbar, IonButton, IonList, IonItem } from '@ionic/vue';
import { NrfMesh, PermissionKey, PermissionState, MeshDevice, Bool, SensorValue, Int24Value } from "@capacitor/autorunify-nrfmesh";
import { onMounted, ref } from 'vue';

const devicesRef = ref<Array<MeshDevice>>([])

async function onRequestPermissions() {
  const bleEnabled = await NrfMesh.isBluetoothEnabled()
  if (!bleEnabled.enabled) {
    console.log("BLE is not enabled")
    return
  }

  const permis = await NrfMesh.checkPermissions()
  const denied = Object.keys(permis).map(key => permis[key as PermissionKey]).filter(v => (v as PermissionState) == 'denied')

  console.log(denied)
  if (denied.length > 0) {
    await NrfMesh.requestPermissions()
  }
}

async function onInitMeshNetwork() {
  console.log(await NrfMesh.init());

  const { netkeys } = await NrfMesh.netkeys()
  if (netkeys.length >= 2) return

  const d0 = await NrfMesh.netkey({ add: {} })
  console.log(d0)
}

async function onDeviceScanner() {
  devicesRef.value = []

  const { devices } = await NrfMesh.devices()
  devicesRef.value = devices
  console.log(devices)
}

async function onDeviceBind(dev: MeshDevice) {
  const d0 = await NrfMesh.identify({
    uuid: dev.uuid as string,
    address: dev.address
  })

  console.log(d0)

  const d1 = await NrfMesh.provision({
    uuid: dev.uuid as string,
    address: dev.address
  })

  console.log(d1)

  if (!d1.completed) return

  const d2 = await NrfMesh.composition({
    timeout: 20,
    unicastAddress: d1.unicastAddress as number
  })

  console.log(d2)
}

async function onConnectToProxy() {
  const d0 = await NrfMesh.connect()
  console.log(d0)
}

async function onTestNetwork() {
  const { nodes } = await NrfMesh.nodes()
  console.log(nodes)

  const { appkeys } = await NrfMesh.appkeys()
  const { netkeys } = await NrfMesh.netkeys()
  console.log(netkeys)

  const appkey = appkeys.find((v) => v.index >= 0)
  if (!appkey) return

  const netkey = netkeys.find((v) => v.index > 0)
  console.log(netkey)

  const node = nodes.find((v) => v.unicastAddress > 1)
  if (!node) return

  const element = node.elements[1]
  if (!element) return

  const d3 = await NrfMesh.node({
    unicastAddress: node.unicastAddress,
    heartbeat: {
      pub: {
        set: {
          period: 8,
          count: 4,
          ttl: 6
        }
      }
    }
  })
  console.log(d3)

  for (const model of element.models) {
    if (model.appkeys.length == 0) {
      const d1 = await NrfMesh.node({
        unicastAddress: node.unicastAddress,
        appkey: {
          add: {
            appkeyIndex: appkey.index
          }
        },
        bind: {
          add: {
            elementAddress: element.elementAddress,
            modelId: model.modelId,
            appkeyIndex: appkey.index
          }
        }
      })
      console.log(d1)
    }
  }

  const d0 = await NrfMesh.model({
    elementAddress: element.elementAddress,
    appkeyIndex: appkey.index,
    onoff: {}
  })

  console.log(d0)

  const d2 = await NrfMesh.model({
    elementAddress: element.elementAddress,
    appkeyIndex: appkey.index,
    onoff: {
      set: {
        state: !d0.onoff?.state
      }
    }
  })

  console.log(d2)

}

onMounted(() => {
  NrfMesh.addListener('state', (e) => {
    console.log(e)
  })

  NrfMesh.addListener('node', (e) => {
    console.log(e)
  })
})

</script>

<style scoped>
#container {
  text-align: center;

  position: absolute;
  left: 0;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
}

#container strong {
  font-size: 20px;
  line-height: 26px;
}

#container p {
  font-size: 16px;
  line-height: 22px;

  color: #8c8c8c;

  margin: 0;
}

#container a {
  text-decoration: none;
}
</style>
