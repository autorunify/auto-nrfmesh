import { SensorRawValue } from './definitions'

export function unsignedToSigned(unsigned: number, size: number) {
    if ((unsigned & (1 << (size - 1))) != 0) {
        unsigned = -1 * ((1 << (size - 1)) - (unsigned & ((1 << (size - 1)) - 1)));
    }
    return unsigned;
}

export function signedToUnsigned(signed: number, size: number) {
    if (signed < 0) {
        signed = (1 << size) + signed;
        signed &= (1 << size) - 1;
    }
    return signed;
}

export type SensorValueType = boolean | number | Date | string | Uint8Array;

export interface SensorValueConstructor {
    new(propertyId: number, ...args: any): SensorValue<SensorValueType>;
}

export abstract class SensorValue<T extends SensorValueType> {
    private static types: Map<number, { args: any, CLS: SensorValueConstructor }> = new Map()

    public static register(propertyId: number, cls: SensorValueConstructor, ...args: any) {
        SensorValue.types.set(propertyId, { args, CLS: cls })
    }

    public static from(src: SensorRawValue | number): SensorValue<SensorValueType> {
        if (typeof src === 'number') {
            const TP = SensorValue.types.get(src)
            if (TP) return new TP.CLS(src, ...TP.args)

            return new Unknown(src)
        }

        const ins = SensorValue.from(src.propertyId)
        ins.setValue(Uint8Array.from(src.bytes))
        return ins
    }

    private _propertyId: number;
    protected _value: T | null;

    public get propertyId() {
        return this._propertyId;
    }

    public get value() {
        return this._value;
    }

    protected constructor(propertyId: number) {
        this._propertyId = propertyId
        this._value = null
    }

    protected isLittleEndian() {
        const ua = new ArrayBuffer(4);
        const u8a = new Uint8Array(ua);
        const u32a = new Uint32Array(ua);

        u32a[0] = 0x01020304;
        return u8a[0] == 0x01;
    }

    public abstract setValue(value: T | Uint8Array): void;
    public abstract toBytes(): Uint8Array;

    public toRawValue(): SensorRawValue {
        const values: Array<number> = [];
        this.toBytes().forEach(v => values.push(v));

        return {
            propertyId: this.propertyId,
            bytes: values
        };
    }
}

export class Bool extends SensorValue<boolean> {
    public constructor(propertyId: number) {
        super(propertyId);
    }

    public setValue(value: boolean | Uint8Array): void {
        if (typeof value == 'boolean') {
            this._value = value;
            return;
        }

        if (value.byteLength == 0) this._value = false;
        else this._value = value[0] == 0x01;
    }

    public toBytes(): Uint8Array {
        const u8a = new Uint8Array(1);
        u8a[0] = this._value ? 0x01 : 0x00;
        return u8a;
    }
}

export class Percentage8 extends SensorValue<number> {
    public constructor(propertyId: number) {
        super(propertyId);
    }

    public setValue(value: number | Uint8Array): void {
        if (typeof value == 'number') {
            this._value = value;
            return;
        }

        this._value = unsignedToSigned(value[0], 8) / 2.0;
    }

    public toBytes(): Uint8Array {
        const val = parseInt(((this._value || 0) * 2.0).toString());
        const u8a = new Uint8Array(1);

        u8a[0] = (val >> 0) & 0xff;

        return u8a;
    }
}

export class Temperature extends SensorValue<number> {
    private _type: 'Celsius' | 'Fahrenheit';

    public get type() {
        return this._type;
    }

    public constructor(propertyId: number, type?: 'Celsius' | 'Fahrenheit') {
        super(propertyId);
        this._type = type || 'Celsius';
    }

    public setValue(value: number | Uint8Array): void {
        if (typeof value == 'number') {
            this._value = value;
            return;
        }

        const u8a = value;
        let tval;
        if (u8a.byteLength == 1) {
            tval = unsignedToSigned(u8a[0], 8);
            if (tval == 0x8000) this._value = null;
            else this._value = tval / 2.0;
            this._type = 'Celsius';
        } else if (u8a.byteLength == 2) {
            tval = unsignedToSigned((u8a[1] << 8) | u8a[0], 16);
            if (tval == 0x8000) this._value = null;
            else this._value = tval / 100.0;
            this._type = 'Fahrenheit';
        }
    }

    public toBytes(): Uint8Array {
        const u8a = new Uint8Array(this._type == 'Celsius' ? 1 : 2);
        if (this._type == 'Celsius') {
            const val = parseInt(((this._value || 0) * 2).toString());
            u8a[0] = val & 0xff;
        } else {
            const val = parseInt(((this._value || 0) * 100).toString());
            u8a[0] = (val >> 0) & 0xff;
            u8a[1] = (val >> 8) & 0xff;
        }
        return u8a;
    }
}

export class Count extends SensorValue<number> {
    public constructor(propertyId: number) {
        super(propertyId);
    }

    public setValue(value: number | Uint8Array): void {
        if (typeof value == 'number') {
            this._value = value;
            return;
        }

        this._value = 0;
        if (value.byteLength == 2) {
            this._value |= value[0] << 0;
            this._value |= value[1] << 8;
        } else if (value.byteLength == 3) {
            this._value |= value[0] << 16;
            this._value |= value[1] << 8;
            this._value |= value[2] << 0;
        }
    }
    public toBytes(): Uint8Array {
        const m16 = 0xffff;
        const val = parseInt((this._value || 0).toString());
        const u8a = new Uint8Array(val > m16 ? 3 : 2);

        if (val > m16) {
            u8a[0] = (val >> 16) & 0xff;
            u8a[1] = (val >> 8) & 0xff;
            u8a[2] = (val >> 0) & 0xff;
        } else {
            u8a[0] = (val >> 0) & 0xff;
            u8a[1] = (val >> 8) & 0xff;
        }

        return u8a;
    }
}

export class Humidity extends SensorValue<number> {
    public constructor(propertyId: number) {
        super(propertyId);
    }

    public setValue(value: number | Uint8Array): void {
        if (typeof value == 'number') {
            this._value = value;
            return;
        }

        this._value = 0;
        this._value |= value[0] << 0;
        this._value |= value[1] << 8;
        this._value /= 100.0;
    }
    public toBytes(): Uint8Array {
        const val = parseInt(((this._value || 0) * 100).toString());
        const u8a = new Uint8Array(2);

        u8a[0] = (val >> 0) & 0xff;
        u8a[1] = (val >> 8) & 0xff;

        return u8a;
    }
}

export class PerceivedLightness extends SensorValue<number> {
    public constructor(propertyId: number) {
        super(propertyId);
    }

    public setValue(value: number | Uint8Array): void {
        if (typeof value == 'number') {
            this._value = value;
            return;
        }

        this._value = 0;
        this._value |= value[0] << 0;
        this._value |= value[1] << 8;
    }
    public toBytes(): Uint8Array {
        const val = parseInt((this._value || 0).toString());
        const u8a = new Uint8Array(2);

        u8a[0] = (val >> 0) & 0xff;
        u8a[1] = (val >> 8) & 0xff;

        return u8a;
    }
}

export class TimeSecond extends SensorValue<number> {
    public constructor(propertyId: number) {
        super(propertyId);
    }

    public setValue(value: number | Uint8Array): void {
        if (typeof value == 'number') {
            this._value = value;
            return;
        }

        const len = value.byteLength;
        this._value = 0;
        if (len == 1) {
            this._value |= value[0];
        } else if (len == 2) {
            this._value |= value[0] << 0;
            this._value |= value[1] << 8;
        } else if (len == 4) {
            this._value |= value[0] << 0;
            this._value |= value[1] << 8;
            this._value |= value[2] << 16;
            this._value |= value[3] << 24;
        }
    }
    public toBytes(): Uint8Array {
        const val = this._value || 0;
        let len = 1;
        if (val > 0xffff) len = 2;
        if (val > 0xffffffff) len = 4;

        const u8a = new Uint8Array(len);
        if (len == 1) {
            u8a[0] = (val >> 0) & 0xff;
        } else if (len == 2) {
            u8a[0] = (val >> 0) & 0xff;
            u8a[1] = (val >> 8) & 0xff;
        } else if (len == 4) {
            u8a[0] = (val >> 0) & 0xff;
            u8a[1] = (val >> 8) & 0xff;
            u8a[2] = (val >> 16) & 0xff;
            u8a[3] = (val >> 24) & 0xff;
        }
        return u8a;
    }
}

export class Illuminance extends SensorValue<number> {
    public constructor(propertyId: number) {
        super(propertyId);
    }

    public setValue(value: number | Uint8Array): void {
        if (typeof value == 'number') {
            this._value = value;
            return;
        }

        this._value = 0;
        this._value |= value[0] << 0;
        this._value |= value[1] << 8;
        this._value |= value[2] << 16;
        this._value /= 100.0;
    }
    public toBytes(): Uint8Array {
        const val = parseInt(((this._value || 0) * 100).toString());
        const u8a = new Uint8Array(3);

        u8a[0] = (val >> 0) & 0xff;
        u8a[1] = (val >> 8) & 0xff;
        u8a[2] = (val >> 16) & 0xff;

        return u8a;
    }
}

export class TimeHour24 extends SensorValue<number> {
    public constructor(propertyId: number) {
        super(propertyId);
    }

    public setValue(value: number | Uint8Array): void {
        if (typeof value == 'number') {
            this._value = value;
            return;
        }

        this._value = 0;
        this._value |= value[0] << 0;
        this._value |= value[1] << 8;
        this._value |= value[2] << 16;
    }
    public toBytes(): Uint8Array {
        const val = parseInt((this._value || 0).toString());
        const u8a = new Uint8Array(3);

        u8a[0] = (val >> 0) & 0xff;
        u8a[1] = (val >> 8) & 0xff;
        u8a[2] = (val >> 16) & 0xff;

        return u8a;
    }
}

export class TimeMillisecond24 extends SensorValue<number> {
    public constructor(propertyId: number) {
        super(propertyId);
    }

    public setValue(value: number | Uint8Array): void {
        if (typeof value == 'number') {
            this._value = value;
            return;
        }

        this._value = 0;
        this._value |= value[0] << 0;
        this._value |= value[1] << 8;
        this._value |= value[2] << 16;
        this._value /= 1000.0;
    }
    public toBytes(): Uint8Array {
        const val = parseInt(((this._value || 0) * 1000).toString());
        const u8a = new Uint8Array(3);

        u8a[0] = (val >> 0) & 0xff;
        u8a[1] = (val >> 8) & 0xff;
        u8a[2] = (val >> 16) & 0xff;

        return u8a;
    }
}

export class DateUtc extends SensorValue<Date> {
    public constructor(propertyId: number) {
        super(propertyId);
    }

    public setValue(value: Date | Uint8Array): void {
        if (value instanceof Date) {
            this._value = value;
            return;
        }

        let val = 0;
        val |= value[0] << 0;
        val |= value[1] << 8;
        val |= value[2] << 16;
        val *= 86400000;
        this._value = new Date(val);
    }
    public toBytes(): Uint8Array {
        const time = (this._value as Date).getTime();
        const val = parseInt((time / 86400000).toString());
        const u8a = new Uint8Array(3);

        u8a[0] = (val >> 0) & 0xff;
        u8a[1] = (val >> 8) & 0xff;
        u8a[2] = (val >> 16) & 0xff;

        return u8a;
    }
}

export class Pressure extends SensorValue<number> {
    public constructor(propertyId: number) {
        super(propertyId);
    }

    public setValue(value: number | Uint8Array): void {
        if (typeof value == 'number') {
            this._value = value;
            return;
        }

        this._value = 0;
        this._value |= value[0] << 0;
        this._value |= value[1] << 8;
        this._value |= value[2] << 16;
        this._value |= value[3] << 24;
        this._value /= 10.0;
    }
    public toBytes(): Uint8Array {
        const val = parseInt(((this._value || 0) * 10).toString());
        const u8a = new Uint8Array(4);

        u8a[0] = (val >> 0) & 0xff;
        u8a[1] = (val >> 8) & 0xff;
        u8a[2] = (val >> 16) & 0xff;
        u8a[3] = (val >> 24) & 0xff;

        return u8a;
    }
}

export class Coefficient extends SensorValue<number> {
    public constructor(propertyId: number) {
        super(propertyId);
    }

    public setValue(value: number | Uint8Array): void {
        if (typeof value == 'number') {
            this._value = value;
            return;
        }

        if (this.isLittleEndian()) {
            const f32a = new Float32Array(value);
            this._value = f32a[0];
        } else {
            const f32a = new Float32Array(value.reverse());
            this._value = f32a[0];
        }
    }
    public toBytes(): Uint8Array {
        const val = this._value as number;
        const ua = new ArrayBuffer(4);
        const u8a = new Uint8Array(ua);
        const f32a = new Float32Array(ua);

        f32a[0] = val;
        if (this.isLittleEndian()) {
            return u8a;
        } else {
            return u8a.reverse();
        }
    }
}

export class FixedString extends SensorValue<string> {
    public constructor(propertyId: number) {
        super(propertyId);
    }

    public setValue(value: string | Uint8Array): void {
        if (typeof value == 'string') {
            this._value = value;
            return;
        }

        this._value = String.fromCharCode(...value);
    }
    public toBytes(): Uint8Array {
        const vals = (this._value || '').split('');
        const raws = Uint8Array.from(vals.map(c => c.charCodeAt(0)));

        let flen = raws.byteLength;
        let len = 0;
        if (flen > 0 && flen <= 8) {
            len = 8;
        } else if (flen > 8 && flen <= 16) {
            len = 16;
        } else if (flen > 16 && flen <= 24) {
            len = 24;
        } else if (flen > 24 && flen <= 36) {
            len = 36;
        } else if (flen > 36 && flen <= 64) {
            len = 64;
        }

        const u8a = new Uint8Array(len);

        raws.forEach((v, i) => {
            u8a[i] = v;
        });

        return u8a;
    }
}

export class Energy32 extends SensorValue<number> {
    public constructor(propertyId: number) {
        super(propertyId);
    }

    public setValue(value: number | Uint8Array): void {
        if (typeof value == 'number') {
            this._value = value;
            return;
        }

        this._value = 0;
        this._value |= value[0] << 0;
        this._value |= value[1] << 8;
        this._value |= value[2] << 16;
        this._value |= value[3] << 24;
        this._value /= 1000.0;
    }
    public toBytes(): Uint8Array {
        const val = parseInt(((this._value || 0) * 1000).toString());
        const u8a = new Uint8Array(4);

        u8a[0] = (val >> 0) & 0xff;
        u8a[1] = (val >> 8) & 0xff;
        u8a[2] = (val >> 16) & 0xff;
        u8a[3] = (val >> 24) & 0xff;

        return u8a;
    }
}

export class Power extends SensorValue<number> {
    public constructor(propertyId: number) {
        super(propertyId);
    }

    public setValue(value: number | Uint8Array): void {
        if (typeof value == 'number') {
            this._value = value;
            return;
        }

        this._value = 0;
        this._value |= value[0] << 0;
        this._value |= value[1] << 8;
        this._value |= value[2] << 16;
        this._value /= 10.0;
    }
    public toBytes(): Uint8Array {
        const val = parseInt(((this._value || 0) * 10).toString());
        const u8a = new Uint8Array(3);

        u8a[0] = (val >> 0) & 0xff;
        u8a[1] = (val >> 8) & 0xff;
        u8a[2] = (val >> 16) & 0xff;

        return u8a;
    }
}

export class ElectricCurrent extends SensorValue<number> {
    public constructor(propertyId: number) {
        super(propertyId);
    }

    public setValue(value: number | Uint8Array): void {
        if (typeof value == 'number') {
            this._value = value;
            return;
        }

        this._value = 0;
        this._value |= value[0] << 0;
        this._value |= value[1] << 8;
        this._value /= 100.0;
    }
    public toBytes(): Uint8Array {
        const val = parseInt(((this._value || 0) * 100).toString());
        const u8a = new Uint8Array(3);

        u8a[0] = (val >> 0) & 0xff;
        u8a[1] = (val >> 8) & 0xff;
        u8a[2] = (val >> 16) & 0xff;

        return u8a;
    }
}

export class Unknown extends SensorValue<Uint8Array> {
    public constructor(propertyId: number) {
        super(propertyId);
        this._value = new Uint8Array(0);
    }

    public setValue(value: Uint8Array): void {
        this._value = value;
    }
    public toBytes(): Uint8Array {
        return this._value as Uint8Array;
    }
}

export class Uint8 extends SensorValue<number> {
    public constructor(propertyId: number) {
        super(propertyId);
    }

    public setValue(value: number | Uint8Array): void {
        if (typeof value == 'number') {
            this._value = value;
            return;
        }

        this._value = 0;
        this._value |= value[0] << 0;
    }

    public toBytes(): Uint8Array {
        const val = parseInt((this._value || 0).toString());
        const u8a = new Uint8Array(1);

        u8a[0] = (val >> 0) & 0xff;

        return u8a;
    }
}

export class Uint16 extends SensorValue<number> {
    public constructor(propertyId: number) {
        super(propertyId);
    }

    public setValue(value: number | Uint8Array): void {
        if (typeof value == 'number') {
            this._value = value;
            return;
        }

        this._value = 0;
        this._value |= value[0] << 0;
        this._value |= value[1] << 8;
    }

    public toBytes(): Uint8Array {
        const val = parseInt((this._value || 0).toString());
        const u8a = new Uint8Array(2);

        u8a[0] = (val >> 0) & 0xff;
        u8a[1] = (val >> 8) & 0xff;

        return u8a;
    }
}

export class Uint32 extends SensorValue<number> {
    public constructor(propertyId: number) {
        super(propertyId);
    }

    public setValue(value: number | Uint8Array): void {
        if (typeof value == 'number') {
            this._value = value;
            return;
        }

        this._value = 0;
        this._value |= value[0] << 0;
        this._value |= value[1] << 8;
        this._value |= value[2] << 16;
        this._value |= value[3] << 24;
    }

    public toBytes(): Uint8Array {
        const val = parseInt((this._value || 0).toString());
        const u8a = new Uint8Array(4);

        u8a[0] = (val >> 0) & 0xff;
        u8a[1] = (val >> 8) & 0xff;
        u8a[2] = (val >> 16) & 0xff;
        u8a[3] = (val >> 24) & 0xff;

        return u8a;
    }
}

export class Int8 extends SensorValue<number> {
    public constructor(propertyId: number) {
        super(propertyId);
    }

    public setValue(value: number | Uint8Array): void {
        if (typeof value == 'number') {
            this._value = value;
            return;
        }

        this._value = 0;
        this._value |= value[0] << 0;
        this._value = unsignedToSigned(this._value, 8);
    }

    public toBytes(): Uint8Array {
        const int = parseInt((this._value || 0).toString());
        const val = signedToUnsigned(int, 8);
        const u8a = new Uint8Array(1);

        u8a[0] = (val >> 0) & 0xff;

        return u8a;
    }
}

export class Int16 extends SensorValue<number> {
    public constructor(propertyId: number) {
        super(propertyId);
    }

    public setValue(value: number | Uint8Array): void {
        if (typeof value == 'number') {
            this._value = value;
            return;
        }

        this._value = 0;
        this._value |= value[0] << 0;
        this._value |= value[1] << 8;
        this._value = unsignedToSigned(this._value, 16);
    }

    public toBytes(): Uint8Array {
        const int = parseInt((this._value || 0).toString());
        const val = signedToUnsigned(int, 16);
        const u8a = new Uint8Array(2);

        u8a[0] = (val >> 0) & 0xff;
        u8a[1] = (val >> 8) & 0xff;

        return u8a;
    }
}

export class Int32 extends SensorValue<number> {
    public constructor(propertyId: number) {
        super(propertyId);
    }

    public setValue(value: number | Uint8Array): void {
        if (typeof value == 'number') {
            this._value = value;
            return;
        }

        this._value = 0;
        this._value |= value[0] << 0;
        this._value |= value[1] << 8;
        this._value |= value[2] << 16;
        this._value |= value[3] << 24;
        this._value = unsignedToSigned(this._value, 32);
    }

    public toBytes(): Uint8Array {
        const int = parseInt((this._value || 0).toString());
        const val = signedToUnsigned(int, 32);
        const u8a = new Uint8Array(4);

        u8a[0] = (val >> 0) & 0xff;
        u8a[1] = (val >> 8) & 0xff;
        u8a[2] = (val >> 16) & 0xff;
        u8a[3] = (val >> 24) & 0xff;

        return u8a;
    }
}

export class Uint16Value extends SensorValue<number> {
    private _exponent: number;

    public get exponent() {
        return this._exponent;
    }

    public constructor(propertyId: number, exponent?: number) {
        super(propertyId);
        this._exponent = exponent || 0;
    }

    public setValue(value: number | Uint8Array): void {
        if (typeof value == 'number') {
            this._value = value;
            return;
        }

        this._value = 0;
        this._value |= value[0] << 0;
        this._value |= value[1] << 8;
        this._value /= Math.pow(10, this._exponent);
    }
    public toBytes(): Uint8Array {
        const int = (this._value || 0) * Math.pow(10, this._exponent);
        const val = parseInt(int.toString());
        const u8a = new Uint8Array(2);

        u8a[0] = (val >> 0) & 0xff;
        u8a[1] = (val >> 8) & 0xff;

        return u8a;
    }
}

export class Uint24Value extends SensorValue<number> {
    private _exponent: number;

    public get exponent() {
        return this._exponent;
    }

    public constructor(propertyId: number, exponent?: number) {
        super(propertyId);
        this._exponent = exponent || 0;
    }

    public setValue(value: number | Uint8Array): void {
        if (typeof value == 'number') {
            this._value = value;
            return;
        }

        this._value = 0;
        this._value |= value[0] << 0;
        this._value |= value[1] << 8;
        this._value |= value[2] << 16;
        this._value /= Math.pow(10, this._exponent);
    }
    public toBytes(): Uint8Array {
        const int = (this._value || 0) * Math.pow(10, this._exponent);
        const val = parseInt(int.toString());
        const u8a = new Uint8Array(3);

        u8a[0] = (val >> 0) & 0xff;
        u8a[1] = (val >> 8) & 0xff;
        u8a[2] = (val >> 16) & 0xff;

        return u8a;
    }
}

export class Uint32Value extends SensorValue<number> {
    private _exponent: number;

    public get exponent() {
        return this._exponent;
    }

    public constructor(propertyId: number, exponent?: number) {
        super(propertyId);
        this._exponent = exponent || 0;
    }

    public setValue(value: number | Uint8Array): void {
        if (typeof value == 'number') {
            this._value = value;
            return;
        }

        this._value = 0;
        this._value |= value[0] << 0;
        this._value |= value[1] << 8;
        this._value |= value[2] << 16;
        this._value |= value[3] << 24;
        this._value /= Math.pow(10, this._exponent);
    }
    public toBytes(): Uint8Array {
        const int = (this._value || 0) * Math.pow(10, this._exponent);
        const val = parseInt(int.toString());
        const u8a = new Uint8Array(4);

        u8a[0] = (val >> 0) & 0xff;
        u8a[1] = (val >> 8) & 0xff;
        u8a[2] = (val >> 16) & 0xff;
        u8a[3] = (val >> 24) & 0xff;

        return u8a;
    }
}

export class Int16Value extends SensorValue<number> {
    private _exponent: number;

    public get exponent() {
        return this._exponent;
    }

    public constructor(propertyId: number, exponent?: number) {
        super(propertyId);
        this._exponent = exponent || 0;
    }

    public setValue(value: number | Uint8Array): void {
        if (typeof value == 'number') {
            this._value = value;
            return;
        }

        this._value = 0;
        this._value |= value[0] << 0;
        this._value |= value[1] << 8;
        this._value = unsignedToSigned(this._value, 16);
        this._value /= Math.pow(10, this._exponent);
    }
    public toBytes(): Uint8Array {
        const int = (this._value || 0) * Math.pow(10, this._exponent);
        const val = signedToUnsigned(parseInt(int.toString()), 16);
        const u8a = new Uint8Array(2);

        u8a[0] = (val >> 0) & 0xff;
        u8a[1] = (val >> 8) & 0xff;

        return u8a;
    }
}

export class Int24Value extends SensorValue<number> {
    private _exponent: number;

    public get exponent() {
        return this._exponent;
    }

    public constructor(propertyId: number, exponent?: number) {
        super(propertyId);
        this._exponent = exponent || 0;
    }

    public setValue(value: number | Uint8Array): void {
        if (typeof value == 'number') {
            this._value = value;
            return;
        }

        this._value = 0;
        this._value |= value[0] << 0;
        this._value |= value[1] << 8;
        this._value |= value[2] << 16;
        this._value = unsignedToSigned(this._value, 24);
        this._value /= Math.pow(10, this._exponent);
    }
    public toBytes(): Uint8Array {
        const int = (this._value || 0) * Math.pow(10, this._exponent);
        const val = signedToUnsigned(parseInt(int.toString()), 24);
        const u8a = new Uint8Array(3);

        u8a[0] = (val >> 0) & 0xff;
        u8a[1] = (val >> 8) & 0xff;
        u8a[2] = (val >> 16) & 0xff;

        return u8a;
    }
}

export class Int32Value extends SensorValue<number> {
    private _exponent: number;

    public get exponent() {
        return this._exponent;
    }

    public constructor(propertyId: number, exponent?: number) {
        super(propertyId);
        this._exponent = exponent || 0;
    }

    public setValue(value: number | Uint8Array): void {
        if (typeof value == 'number') {
            this._value = value;
            return;
        }

        this._value = 0;
        this._value |= value[0] << 0;
        this._value |= value[1] << 8;
        this._value |= value[2] << 16;
        this._value |= value[3] << 24;
        this._value = unsignedToSigned(this._value, 32);
        this._value /= Math.pow(10, this._exponent);
    }
    public toBytes(): Uint8Array {
        const int = (this._value || 0) * Math.pow(10, this._exponent);
        const val = signedToUnsigned(parseInt(int.toString()), 32);
        const u8a = new Uint8Array(4);

        u8a[0] = (val >> 0) & 0xff;
        u8a[1] = (val >> 8) & 0xff;
        u8a[2] = (val >> 16) & 0xff;
        u8a[3] = (val >> 24) & 0xff;

        return u8a;
    }
}
